package by.anatolyloyko.ams.administration.studio.kafka

import by.anatolyloyko.ams.administration.infrastructure.kafka.MyKafkaListener
import by.anatolyloyko.ams.administration.studio.action.CreateStudioAction
import by.anatolyloyko.ams.studio.kafka.schema.EventType
import by.anatolyloyko.ams.studio.kafka.schema.StudioEvent
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.Acknowledgment
import org.springframework.kafka.support.KafkaHeaders
import org.springframework.messaging.handler.annotation.Header
import org.springframework.stereotype.Component

@Component
class StudioKafkaListener(
    private val createStudioAction: CreateStudioAction
) : MyKafkaListener<StudioEvent>() {
    @KafkaListener(topics = ["\${ams.kafka.topic.studio.name}"])
    fun onStudioEvent(
        event: StudioEvent,
        @Header(KafkaHeaders.RECEIVED_KEY) key: String,
        @Header(KafkaHeaders.RECEIVED_TOPIC) topic: String,
        acknowledgment: Acknowledgment
    ) = consumeEvent(event, key, topic, acknowledgment) {
        when (event.type) {
            EventType.CREATED -> createStudioAction(
                studioId = event.id,
                brandId = event.brandId,
                userId = event.userId
            )
        }
    }
}
