package by.anatolyloyko.ams.brand.kafka.action

import by.anatolyloyko.ams.brand.BRAND
import by.anatolyloyko.ams.brand.NEW_BRAND
import by.anatolyloyko.ams.brand.USER_ID
import by.anatolyloyko.ams.brand.kafka.schema.BrandEvent
import by.anatolyloyko.ams.brand.kafka.schema.EventType
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.WithAssertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.kafka.core.KafkaTemplate

const val TOPIC_NAME = "topicName"

class KafkaCreateBrandActionTest : WithAssertions {
    val kafkaTemplate = mockk<KafkaTemplate<Long, BrandEvent>> {
        every { send(any(), any(), any()) } returns mockk(relaxed = true)
    }

    private val action = KafkaCreateBrandAction(
        topicName = TOPIC_NAME,
        kafkaTemplate = kafkaTemplate
    )

    @Test
    fun `must send event`() {
        val brand = BRAND
        val ownerUserId = USER_ID

        val result = action(brand, ownerUserId)

        assertThat(result).isEqualTo(brand.id)
        verify(exactly = 1) {
            kafkaTemplate.send(
                eq(TOPIC_NAME),
                eq(brand.id!!),
                match<BrandEvent> {
                    it.id == brand.id &&
                        it.userId == ownerUserId &&
                        it.type == EventType.CREATED
                }
            )
        }
    }

    @Test
    fun `must throw exception when brand ID is null`() {
        val brand = NEW_BRAND
        val ownerUserId = USER_ID

        assertThrows<IllegalArgumentException> { action(brand, ownerUserId) }

        verify(exactly = 0) {
            kafkaTemplate.send(any(), any(), any())
        }
    }
}
