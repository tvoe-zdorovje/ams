package by.anatolyloyko.ams.administration.role.command

import by.anatolyloyko.ams.administration.NEW_ROLE
import by.anatolyloyko.ams.administration.ROLE_ID
import by.anatolyloyko.ams.administration.STUDIO_ID
import by.anatolyloyko.ams.administration.role.action.SaveRoleAction
import by.anatolyloyko.ams.administration.role.command.input.SaveRoleInput
import by.anatolyloyko.ams.administration.role.model.Role
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.WithAssertions
import org.junit.jupiter.api.Test

class SaveRoleCommandHandlerTest : WithAssertions {
    private val saveRoleAction = mockk<SaveRoleAction> {
        every { this@mockk(any(), any<Role>(), any()) } returns ROLE_ID
    }
    private val handler = SaveRoleCommandHandler(saveRoleAction)

    private val command = SaveRoleCommand(
        input = SaveRoleInput(STUDIO_ID, NEW_ROLE),
    )

    @Test
    fun `must invoke the action`() {
        val result = handler.handle(command)

        assertThat(result).isEqualTo(ROLE_ID)
        verify(exactly = 1) {
            saveRoleAction(
                organizationId = command.input.organizationId,
                role = command.input.role,
                permissions = command.input.permissions
            )
        }
    }
}
