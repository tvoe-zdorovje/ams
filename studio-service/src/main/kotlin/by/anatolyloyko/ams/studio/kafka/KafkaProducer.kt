package by.anatolyloyko.ams.studio.kafka

import by.anatolyloyko.ams.studio.kafka.schema.EventType
import by.anatolyloyko.ams.studio.kafka.schema.StudioEvent
import by.anatolyloyko.ams.studio.model.Studio
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

/**
 * Action responsible for sending a message to the Kafka about the studio creation event.
 *
 * @param topicName the name of a topic which the message is sent to.
 */
@Component("kafkaCreateStudioAction")
class KafkaProducer(
    @param:Value("\${ams.kafka.topic.studio.name}")
    private val topicName: String,

    private val kafkaTemplate: KafkaTemplate<Long, StudioEvent>
) {
    /**
     * Sends a message to the Kafka about the studio creation event.
     *
     * @param studio the studio data.
     * @param ownerUserId identifier of a logged user. This user is considered the owner of the studio is created.
     * @param brandId identifier of a brand which the studio belongs to.
     */
    fun sendStudioCreated(studio: Studio, ownerUserId: Long, brandId: Long): Long {
        val id = requireNotNull(studio.id)
        val data = StudioEvent(id, brandId, ownerUserId, EventType.CREATED)
        kafkaTemplate.send(topicName, id, data).get()

        return id
    }
}