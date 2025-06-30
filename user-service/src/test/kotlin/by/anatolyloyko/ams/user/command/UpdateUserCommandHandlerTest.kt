package by.anatolyloyko.ams.user.command

import by.anatolyloyko.ams.user.USER
import by.anatolyloyko.ams.user.USER_ID
import by.anatolyloyko.ams.user.action.UpdateUserAction
import by.anatolyloyko.ams.user.command.input.UpdateUserCommandInput
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.WithAssertions
import org.junit.jupiter.api.Test

class UpdateUserCommandHandlerTest : WithAssertions {
    private val updateUserAction = mockk<UpdateUserAction> {
        every { this@mockk(any()) } returns USER_ID
    }

    private val handler = UpdateUserCommandHandler(updateUserAction)

    private val command = UpdateUserCommand(
        input = UpdateUserCommandInput(USER),
    )

    @Test
    fun `must invoke the action`() {
        val result = handler.handle(command)

        assertThat(result).isEqualTo(USER_ID)
        verify(exactly = 1) {
            updateUserAction(command.input.user)
        }
    }
}
