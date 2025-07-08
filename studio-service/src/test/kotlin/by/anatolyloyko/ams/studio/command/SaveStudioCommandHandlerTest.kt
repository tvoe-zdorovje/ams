package by.anatolyloyko.ams.studio.command

import by.anatolyloyko.ams.studio.NEW_STUDIO
import by.anatolyloyko.ams.studio.STUDIO_ID
import by.anatolyloyko.ams.studio.action.SaveStudioAction
import by.anatolyloyko.ams.studio.model.Studio
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.WithAssertions
import org.junit.jupiter.api.Test

class SaveStudioCommandHandlerTest : WithAssertions {
    private val saveStudioAction = mockk<SaveStudioAction> {
        every { this@mockk(any<Studio>()) } returns STUDIO_ID
    }
    private val handler = SaveStudioCommandHandler(saveStudioAction)

    private val command = SaveStudioCommand(
        input = NEW_STUDIO,
    )

    @Test
    fun `must invoke the action`() {
        val result = handler.handle(command)

        assertThat(result).isEqualTo(STUDIO_ID)
        verify(exactly = 1) { saveStudioAction(command.input) }
    }
}
