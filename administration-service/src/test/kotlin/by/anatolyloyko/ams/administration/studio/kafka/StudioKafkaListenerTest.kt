package by.anatolyloyko.ams.administration.studio.kafka

import by.anatolyloyko.ams.administration.BRAND_ID
import by.anatolyloyko.ams.administration.STUDIO_ID
import by.anatolyloyko.ams.administration.USER_ID
import by.anatolyloyko.ams.administration.studio.action.CreateStudioAction
import by.anatolyloyko.ams.studio.kafka.schema.EventType
import by.anatolyloyko.ams.studio.kafka.schema.StudioEvent
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.kafka.support.Acknowledgment
import java.time.Duration

class StudioKafkaListenerTest {
    private val createStudioAction = mockk<CreateStudioAction>(relaxed = true)

    private val listener = StudioKafkaListener(createStudioAction)

    private val acknowledgment = mockk<Acknowledgment>(relaxed = true)

    private val createEvent = StudioEvent(STUDIO_ID, BRAND_ID, USER_ID, EventType.CREATED)

    @Test
    fun `must call create studio action for create event and acknowledge message`() {
        listener.onStudioEvent(createEvent, "key-1", "studio-events-topic", acknowledgment)

        verify(exactly = 1) { createStudioAction(STUDIO_ID, BRAND_ID, USER_ID) }
        verify(exactly = 1) { acknowledgment.acknowledge() }
        verify(exactly = 0) { acknowledgment.nack(any<Duration>()) }
    }

    @Test
    fun `must nack when action throws an exception`() {
        every { createStudioAction(any(), any(), any()) } throws IllegalStateException("boom")

        listener.onStudioEvent(createEvent, "key-1", "studio-events-topic", acknowledgment)

        verify(exactly = 1) { createStudioAction(STUDIO_ID, BRAND_ID, USER_ID) }
        verify(exactly = 0) { acknowledgment.acknowledge() }
        verify(exactly = 1) { acknowledgment.nack(any<Duration>()) }
    }
}
