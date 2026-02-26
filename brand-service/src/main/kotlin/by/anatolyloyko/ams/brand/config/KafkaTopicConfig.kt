package by.anatolyloyko.ams.brand.config

import org.apache.kafka.clients.admin.NewTopic
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class KafkaTopicConfig {
    @Bean
    fun brandTopic(
        @Value("\${ams.kafka.topic.brand.name}") name: String,
        @Value("\${ams.kafka.topic.brand.partitions}") partitions: Int,
        @Value("\${ams.kafka.topic.brand.replicas}") replicas: Int,
    ): NewTopic = NewTopic(name, partitions, replicas.toShort())
}
