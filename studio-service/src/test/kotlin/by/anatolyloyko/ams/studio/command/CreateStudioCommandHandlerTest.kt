package by.anatolyloyko.ams.studio.command

import by.anatolyloyko.ams.studio.NEW_STUDIO
import by.anatolyloyko.ams.studio.STUDIO_ID
import by.anatolyloyko.ams.studio.action.CreateStudioAction
import by.anatolyloyko.ams.studio.model.Studio
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.WithAssertions
import org.junit.jupiter.api.Test

class CreateStudioCommandHandlerTest : WithAssertions {
    private val createStudioAction = mockk<CreateStudioAction> {
        every { this@mockk(any<Studio>()) } returns STUDIO_ID
    }
    private val handler = CreateStudioCommandHandler(createStudioAction)

    private val command = CreateStudioCommand(
        input = NEW_STUDIO,
    )

    @Test
    fun `must invoke the action`() {
        val result = handler.handle(command)

        assertThat(result).isEqualTo(STUDIO_ID)
        verify(exactly = 1) { createStudioAction(command.input) }
    }
}
