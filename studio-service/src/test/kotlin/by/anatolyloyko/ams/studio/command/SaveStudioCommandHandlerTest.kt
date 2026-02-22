package by.anatolyloyko.ams.studio.command

import by.anatolyloyko.ams.studio.BRAND_ID
import by.anatolyloyko.ams.studio.NEW_STUDIO
import by.anatolyloyko.ams.studio.STUDIO
import by.anatolyloyko.ams.studio.STUDIO_ID
import by.anatolyloyko.ams.studio.USER_ID
import by.anatolyloyko.ams.studio.action.CreateStudioAction
import by.anatolyloyko.ams.studio.action.UpdateStudioAction
import by.anatolyloyko.ams.studio.kafka.KafkaProducer
import by.anatolyloyko.ams.studio.model.Studio
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.WithAssertions
import org.junit.jupiter.api.Test

class SaveStudioCommandHandlerTest : WithAssertions {
    private val dbCreateStudioAction = mockk<CreateStudioAction> {
        every { this@mockk(any<Studio>()) } returns STUDIO_ID
    }
    private val dbUpdateStudioAction = mockk<UpdateStudioAction> {
        every { this@mockk(any<Studio>(), any()) } returns STUDIO_ID
    }
    private val kafkaProducer = mockk<KafkaProducer> {
        every { sendStudioCreated(any<Studio>(), any(), any()) } returns STUDIO_ID
    }

    private val handler = SaveStudioCommandHandler(
        dbCreateStudioAction = dbCreateStudioAction,
        dbUpdateStudioAction = dbUpdateStudioAction,
        kafkaProducer = kafkaProducer
    )

    @Test
    fun `must invoke create actions if ID is null`() {
        val command = SaveStudioCommand(
            loggedUserId = USER_ID,
            brandId = BRAND_ID,
            input = NEW_STUDIO,
        )

        val result = handler.handle(command)

        assertThat(result).isEqualTo(STUDIO_ID)
        verify(exactly = 1) {
            dbCreateStudioAction(studio = command.input)
        }
        verify(exactly = 1) {
            kafkaProducer.sendStudioCreated(
                studio = eq(command.input.copy(id = STUDIO_ID)),
                ownerUserId = eq(command.loggedUserId),
                brandId = eq(command.brandId)
            )
        }
    }

    @Test
    fun `must invoke update action if ID is not null`() {
        val command = SaveStudioCommand(
            loggedUserId = USER_ID,
            brandId = BRAND_ID,
            input = STUDIO,
        )

        val result = handler.handle(command)

        assertThat(result).isEqualTo(STUDIO_ID)
        verify(exactly = 1) {
            dbUpdateStudioAction(
                studio = command.input,
                userId = command.loggedUserId,
            )
        }
    }
}
