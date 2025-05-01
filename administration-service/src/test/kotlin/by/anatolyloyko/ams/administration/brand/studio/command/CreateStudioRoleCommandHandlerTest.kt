package by.anatolyloyko.ams.administration.brand.studio.command

import by.anatolyloyko.ams.administration.BRAND_ID
import by.anatolyloyko.ams.administration.NEW_ROLE
import by.anatolyloyko.ams.administration.PERMISSION
import by.anatolyloyko.ams.administration.ROLE_ID
import by.anatolyloyko.ams.administration.brand.studio.action.CreateStudioRoleAction
import by.anatolyloyko.ams.administration.brand.studio.command.input.CreateStudioRoleInput
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.WithAssertions
import org.junit.jupiter.api.Test

class CreateStudioRoleCommandHandlerTest : WithAssertions {
    private val action = mockk<CreateStudioRoleAction>(relaxed = true) {
        every { this@mockk(any(), any(), any()) } returns ROLE_ID
    }

    private val handler = CreateStudioRoleCommandHandler(action)

    private val command = CreateStudioRoleCommand(
        input = CreateStudioRoleInput(
            studioId = BRAND_ID,
            role = NEW_ROLE,
            permissions = listOf(PERMISSION.id),
        )
    )

    @Test
    fun `must invoke the action and return result`() {
        val result = handler.handle(command)

        assertThat(result).isEqualTo(ROLE_ID)
        verify(exactly = 1) {
            action(
                studioId = command.input.studioId,
                role = command.input.role,
                permissions = command.input.permissions
            )
        }
    }
}
