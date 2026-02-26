package by.anatolyloyko.ams.studio.config

import org.apache.kafka.clients.admin.NewTopic
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class KafkaTopicConfig {
    @Bean
    fun studioTopic(
        @Value("\${ams.kafka.topic.studio.name}") name: String,
        @Value("\${ams.kafka.topic.studio.partitions}") partitions: Int,
        @Value("\${ams.kafka.topic.studio.replicas}") replicas: Int,
    ): NewTopic = NewTopic(name, partitions, replicas.toShort())
}
