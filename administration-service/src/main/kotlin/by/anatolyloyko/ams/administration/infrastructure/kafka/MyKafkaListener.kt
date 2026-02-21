package by.anatolyloyko.ams.administration.infrastructure.kafka

import by.anatolyloyko.ams.common.infrastructure.logging.log
import org.apache.avro.specific.SpecificRecord
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.support.Acknowledgment
import org.springframework.kafka.support.KafkaHeaders
import org.springframework.messaging.handler.annotation.Header
import java.time.Duration

abstract class MyKafkaListener<T : SpecificRecord> {
    @Value("\${ams.kafka.props.nack-cooldown}")
    private var nackCooldown: Duration = Duration.ZERO

    fun consumeEvent(
        event: T,
        @Header(KafkaHeaders.RECEIVED_KEY) key: String,
        @Header(KafkaHeaders.RECEIVED_TOPIC) topic: String,
        acknowledgment: Acknowledgment,
        action: T.() -> Unit
    ) = try {
        log.info("Consuming an event [key={}] from Kafka topic [{}]: {}", key, topic, event)
        event.action()
        acknowledgment.acknowledge()
    } catch (e: Exception) {
        log.error("Failed to process an event [key=$key]", e)
        acknowledgment.nack(nackCooldown)
    }
}
