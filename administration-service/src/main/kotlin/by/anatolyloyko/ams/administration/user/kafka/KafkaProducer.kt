package by.anatolyloyko.ams.administration.user.kafka

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

internal const val UPDATE_ROLES_EVENT_TYPE = "UPDATE_ROLES"

/**
 * Action responsible for sending user-related event messages to the Kafka.
 *
 * @param topicName the name of a topic which the message is sent to.
 */
@Component
class KafkaProducer(
    @param:Value("\${ams.kafka.topic.administration-user.name}")
    private val topicName: String,

    private val kafkaTemplate: KafkaTemplate<Long, String>,
    private val objectMapper: ObjectMapper
) {
    /**
     * Sends a message to the Kafka about the user role update event.
     *
     * @param userId ID of an user whose role list was changed.
     */
    fun sendRolesUpdated(userId: Long) {
        kafkaTemplate
            .send(
                topicName,
                userId,
                objectMapper.writeValueAsString(
                    mapOf(
                        "time" to System.currentTimeMillis(),
                        "type" to UPDATE_ROLES_EVENT_TYPE,
                        "userId" to userId
                    )
                )
            )
            .get()
    }
}
