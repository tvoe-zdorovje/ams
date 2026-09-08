package by.anatolyloyko.ams.administration.user.kafka

import by.anatolyloyko.ams.administration.USER_ID
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.WithAssertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.kafka.core.KafkaTemplate

const val TOPIC_NAME = "topicName"

class KafkaProducerTest : WithAssertions {
    private val kafkaTemplate = mockk<KafkaTemplate<Long, String>>()

    private val objectMapper = jacksonObjectMapper()

    private val producer = KafkaProducer(
        topicName = TOPIC_NAME,
        kafkaTemplate = kafkaTemplate,
        objectMapper
    )

    @Test
    fun `must send UPDATE_ROLES event`() {
        every { kafkaTemplate.send(any(), any(), any()) } returns mockk(relaxed = true)

        val userId = USER_ID
        producer.sendRolesUpdated(userId)

        val stringCaptor = mutableListOf<String>()
        verify(exactly = 1) {
            kafkaTemplate.send(
                eq(TOPIC_NAME),
                eq(userId),
                capture(stringCaptor),
            )
        }
        val payload = objectMapper.readTree(stringCaptor[0])
        assertThat(payload.path("type").asText()).isEqualTo(UPDATE_ROLES_EVENT_TYPE)
        assertThat(payload.path("userId").asLong()).isEqualTo(userId)
    }

    @Test
    fun `must throw up exception`() {
        every { kafkaTemplate.send(any(), any(), any()) } throws RuntimeException()

        assertThrows<RuntimeException> { producer.sendRolesUpdated(USER_ID) }
    }
}
