package by.anatolyloyko.ams.studio.kafka

import by.anatolyloyko.ams.studio.BRAND_ID
import by.anatolyloyko.ams.studio.NEW_STUDIO
import by.anatolyloyko.ams.studio.STUDIO
import by.anatolyloyko.ams.studio.USER_ID
import by.anatolyloyko.ams.studio.kafka.schema.EventType
import by.anatolyloyko.ams.studio.kafka.schema.StudioEvent
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.WithAssertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.kafka.core.KafkaTemplate

private const val TOPIC_NAME = "topicName"

class KafkaProducerTest : WithAssertions {
    private val kafkaTemplate = mockk<KafkaTemplate<Long, StudioEvent>> {
        every { send(any(), any(), any()) } returns mockk(relaxed = true)
    }

    private val action = KafkaProducer(
        topicName = TOPIC_NAME,
        kafkaTemplate = kafkaTemplate
    )

    @Test
    fun `must send event`() {
        val result = action.sendStudioCreated(
            studio = STUDIO,
            ownerUserId = USER_ID,
            brandId = BRAND_ID
        )

        assertThat(result).isEqualTo(STUDIO.id)
        verify(exactly = 1) {
            kafkaTemplate.send(
                eq(TOPIC_NAME),
                eq(STUDIO.id!!),
                match<StudioEvent> {
                    it.id == STUDIO.id &&
                        it.brandId == BRAND_ID &&
                        it.userId == USER_ID &&
                        it.type == EventType.CREATED
                }
            )
        }
    }

    @Test
    fun `must throw exception when studio ID is null`() {
        assertThrows<IllegalArgumentException> { action.sendStudioCreated(NEW_STUDIO, USER_ID, BRAND_ID) }

        verify(exactly = 0) {
            kafkaTemplate.send(any(), any(), any())
        }
    }
}
