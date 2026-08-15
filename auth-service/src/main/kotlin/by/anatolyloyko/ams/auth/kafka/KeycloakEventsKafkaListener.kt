package by.anatolyloyko.ams.auth.kafka

import by.anatolyloyko.ams.auth.token.action.PublishTokenAction
import by.anatolyloyko.ams.auth.token.command.GenerateTokenCommand
import by.anatolyloyko.ams.auth.token.command.TokenCommandHandler
import by.anatolyloyko.ams.auth.token.command.input.GenerateTokenCommandInput
import by.anatolyloyko.ams.auth.user.finder.UserFinder
import by.anatolyloyko.ams.auth.user.model.User
import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.Acknowledgment
import org.springframework.stereotype.Component
import java.time.Duration

private const val LOGIN_EVENT_TYPE = "LOGIN"

private const val EXTERNAL_USER_ID_PARAM_NAME = "userId"

/**
 * Kafka Consumer listening events produced by Keycloak
 */
@Component
class KeycloakEventsKafkaListener(
    private val objectMapper: ObjectMapper,
    private val userFinder: UserFinder,
    private val tokenCommandHandler: TokenCommandHandler,
    private val publishTokenAction: PublishTokenAction,
) {
    private val log = LoggerFactory.getLogger(this::class.java)

    @Value("\${ams.kafka.props.nack-cooldown}")
    private var nackCooldown: Duration = Duration.ZERO

    /**
     * Kafka Listener handling events from keycloak-events topic.
     *
     * Accepts [LOGIN_EVENT_TYPE] events, generates token containing data about logged user and publishes it to Redis.
     */
    @KafkaListener(topics = ["\${ams.kafka.topic.keycloak-events.name}"])
    fun onKeycloakEvent(
        record: ConsumerRecord<String, String>,
        acknowledgment: Acknowledgment
    ) {
        try {
            val event = objectMapper.readTree(record.value())
            val eventType = event.path("type").asText()
            if (eventType != LOGIN_EVENT_TYPE) {
                return acknowledgment.acknowledge()
            }

            val externalId = event.path(EXTERNAL_USER_ID_PARAM_NAME).asText()

            log.info(
                "Consumed Keycloak {} event [key={}, topic={}, user ID={}]",
                eventType,
                record.key(),
                record.topic(),
                externalId
            )

            publishTokenAction(
                key = externalId,
                token = generateToken(
                    user = findUser(externalId),
                )
            )

            acknowledgment.acknowledge()
        } catch (e: Exception) {
            log.error("Failed to process a Keycloak event [key=${record.key()}]", e)
            acknowledgment.nack(nackCooldown)
        }
    }

    private fun findUser(externalUserId: String): User = userFinder.byIdpUUID(externalUserId)
        ?: throw RuntimeException("No user found with ID = $externalUserId")

    private fun generateToken(user: User): String = tokenCommandHandler.handle(
        GenerateTokenCommand(
            input = GenerateTokenCommandInput(user)
        )
    )
}
