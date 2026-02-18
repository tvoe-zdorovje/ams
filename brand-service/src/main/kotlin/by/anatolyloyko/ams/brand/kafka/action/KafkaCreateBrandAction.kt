package by.anatolyloyko.ams.brand.kafka.action

import by.anatolyloyko.ams.brand.action.CreateBrandAction
import by.anatolyloyko.ams.brand.kafka.schema.BrandEvent
import by.anatolyloyko.ams.brand.kafka.schema.EventType
import by.anatolyloyko.ams.brand.model.Brand
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

/**
 * Action responsible for sending a message to the Kafka about the brand creation event.
 *
 * @param topicName the name of a topic which the message is sent to.
 */
@Component("kafkaCreateBrandAction")
class KafkaCreateBrandAction(
    @param:Value("\${ams.kafka.topic.brand.name}")
    private val topicName: String,

    private val kafkaTemplate: KafkaTemplate<Long, BrandEvent>
) : CreateBrandAction {
    /**
     * Sends a message to the Kafka about the brand creation event.
     *
     * @param brand the brand data.
     * @param ownerUserId identifier of a logged user. This user is considered the owner of the brand is created.
     * @return the brand ID.
     */
    override fun invoke(brand: Brand, ownerUserId: Long): Long {
        val id = requireNotNull(brand.id)
        val data = BrandEvent(id, ownerUserId, EventType.CREATED)
        kafkaTemplate.send(topicName, id, data).get()

        return id
    }
}
