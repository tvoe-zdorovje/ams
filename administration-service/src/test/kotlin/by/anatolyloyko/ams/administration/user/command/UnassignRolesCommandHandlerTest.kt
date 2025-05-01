package by.anatolyloyko.ams.administration.user.command

import by.anatolyloyko.ams.administration.ROLE_ID
import by.anatolyloyko.ams.administration.USER_ID
import by.anatolyloyko.ams.administration.user.action.UnassignRolesAction
import by.anatolyloyko.ams.administration.user.command.input.UserRolesInput
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.WithAssertions
import org.junit.jupiter.api.Test

class UnassignRolesCommandHandlerTest : WithAssertions {
    private val action = mockk<UnassignRolesAction>(relaxed = true)

    private val handler = UnassignRolesCommandHandler(action)

    private val command = UnassignRolesCommand(
        input = UserRolesInput(
            userId = USER_ID,
            roles = listOf(ROLE_ID)
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
