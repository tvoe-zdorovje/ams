package by.anatolyloyko.ams.user.kafka

import by.anatolyloyko.ams.common.infrastructure.logging.log
import by.anatolyloyko.ams.user.action.CreateUserAction
import by.anatolyloyko.ams.user.model.User
import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.Acknowledgment
import org.springframework.stereotype.Component
import java.time.Duration

private const val REGISTER_EVENT_TYPE = "REGISTER"

private const val PHONE_NUMBER_PARAM_NAME = "username"
private const val FIRST_NAME_PARAM_NAME = "first_name"
private const val LAST_NAME_PARAM_NAME = "last_name"
private const val EXTERNAL_USER_ID_PARAM_NAME = "userId"

@Component
class KeycloakEventsKafkaListener(
    private val objectMapper: ObjectMapper,
    private val createUserAction: CreateUserAction
) {
    @Value("\${ams.kafka.props.nack-cooldown}")
    private var nackCooldown: Duration = Duration.ZERO

    @KafkaListener(topics = ["\${ams.kafka.topic.keycloak-events.name}"])
    fun onKeycloakEvent(
        record: ConsumerRecord<String, String>,
        acknowledgment: Acknowledgment
    ) = try {
        val registerUserData = extractRegisterUserData(record.value())
            ?: return acknowledgment.acknowledge()
        val externalId = requireNotNull(registerUserData[EXTERNAL_USER_ID_PARAM_NAME])

        log.info(
            "Consumed Keycloak REGISTER event [key={}, topic={}, user ID={}]",
            record.key(),
            record.topic(),
            externalId
        )

        createUserAction(
            user = User(
                firstName = requireNotNull(registerUserData[FIRST_NAME_PARAM_NAME]),
                lastName = requireNotNull(registerUserData[LAST_NAME_PARAM_NAME]),
                phoneNumber = requireNotNull(registerUserData[PHONE_NUMBER_PARAM_NAME])
            ),
            externalId = externalId
        )

        acknowledgment.acknowledge()
    } catch (e: Exception) {
        log.error("Failed to process a Keycloak event [key=${record.key()}]", e)
        acknowledgment.nack(nackCooldown)
    }

    private fun extractRegisterUserData(eventJson: String): Map<String, String>? {
        val event = objectMapper.readTree(eventJson)

        if (event.path("type").asText() != REGISTER_EVENT_TYPE) {
            return null
        }

        val details = event.path("details")

        return mapOf<String, String>(
            PHONE_NUMBER_PARAM_NAME to details.path(PHONE_NUMBER_PARAM_NAME).asText(),
            FIRST_NAME_PARAM_NAME to details.path(FIRST_NAME_PARAM_NAME).asText(),
            LAST_NAME_PARAM_NAME to details.path(LAST_NAME_PARAM_NAME).asText(),
            EXTERNAL_USER_ID_PARAM_NAME to event.path(EXTERNAL_USER_ID_PARAM_NAME).asText()
        )
    }
}
