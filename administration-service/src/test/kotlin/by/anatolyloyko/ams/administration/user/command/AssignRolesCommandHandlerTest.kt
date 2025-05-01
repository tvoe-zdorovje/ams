package by.anatolyloyko.ams.administration.user.command

import by.anatolyloyko.ams.administration.ROLE_ID
import by.anatolyloyko.ams.administration.USER_ID
import by.anatolyloyko.ams.administration.user.action.AssignRolesAction
import by.anatolyloyko.ams.administration.user.command.input.UserRolesInput
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.WithAssertions
import org.junit.jupiter.api.Test

class AssignRolesCommandHandlerTest : WithAssertions {
    private val action = mockk<AssignRolesAction>(relaxed = true)

    private val handler = AssignRolesCommandHandler(action)

    private val command = AssignRolesCommand(
        input = UserRolesInput(
            userId = USER_ID,
            roles = listOf(ROLE_ID),
        )
    )

    @Test
    fun `must invoke the action`() {
        handler.handle(command)

        verify(exactly = 1) {
            action(
                userId = command.input.userId,
                roles = command.input.roles
            )
        }
    }
}
