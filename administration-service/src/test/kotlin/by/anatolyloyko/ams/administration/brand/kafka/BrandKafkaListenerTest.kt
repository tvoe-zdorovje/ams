package by.anatolyloyko.ams.administration.brand.kafka

import by.anatolyloyko.ams.administration.BRAND_ID
import by.anatolyloyko.ams.administration.USER_ID
import by.anatolyloyko.ams.administration.brand.action.CreateBrandAction
import by.anatolyloyko.ams.brand.kafka.schema.BrandEvent
import by.anatolyloyko.ams.brand.kafka.schema.EventType
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.kafka.support.Acknowledgment
import java.time.Duration

class BrandKafkaListenerTest {
    private val createBrandAction = mockk<CreateBrandAction>(relaxed = true)

    private val listener = BrandKafkaListener(createBrandAction)

    private val acknowledgment = mockk<Acknowledgment>(relaxed = true)

    private val creatEvent = BrandEvent(BRAND_ID, USER_ID, EventType.CREATED)

    @Test
    fun `must call create brand action for create event and acknowledge message`() {
        listener.onBrandEvent(creatEvent, "key-1", "brand-events-topic", acknowledgment)

        verify(exactly = 1) { createBrandAction(BRAND_ID, USER_ID) }
        verify(exactly = 1) { acknowledgment.acknowledge() }
        verify(exactly = 0) { acknowledgment.nack(any<Duration>()) }
    }

    @Test
    fun `must nack when action throws an exception`() {
        every { createBrandAction(any(), any()) } throws IllegalStateException("boom")

        listener.onBrandEvent(creatEvent, "key-1", "brand-events-topic", acknowledgment)

        verify(exactly = 1) { createBrandAction(BRAND_ID, USER_ID) }
        verify(exactly = 0) { acknowledgment.acknowledge() }
        verify(exactly = 1) { acknowledgment.nack(any<Duration>()) }
    }
}
