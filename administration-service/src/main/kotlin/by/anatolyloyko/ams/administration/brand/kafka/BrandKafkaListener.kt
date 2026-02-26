package by.anatolyloyko.ams.administration.brand.kafka

import by.anatolyloyko.ams.administration.brand.action.CreateBrandAction
import by.anatolyloyko.ams.administration.infrastructure.kafka.MyKafkaListener
import by.anatolyloyko.ams.brand.kafka.schema.BrandEvent
import by.anatolyloyko.ams.brand.kafka.schema.EventType
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.Acknowledgment
import org.springframework.kafka.support.KafkaHeaders
import org.springframework.messaging.handler.annotation.Header
import org.springframework.stereotype.Component

@Component
class BrandKafkaListener(
    private val createBrandAction: CreateBrandAction
) : MyKafkaListener<BrandEvent>() {
    @KafkaListener(topics = ["\${ams.kafka.topic.brand.name}"])
    fun onBrandEvent(
        event: BrandEvent,
        @Header(KafkaHeaders.RECEIVED_KEY) key: String,
        @Header(KafkaHeaders.RECEIVED_TOPIC) topic: String,
        acknowledgment: Acknowledgment
    ) = consumeEvent(event, key, topic, acknowledgment) {
        when (event.type) {
            EventType.CREATED -> createBrandAction(brandId = event.id, userId = event.userId)
        }
    }
}
