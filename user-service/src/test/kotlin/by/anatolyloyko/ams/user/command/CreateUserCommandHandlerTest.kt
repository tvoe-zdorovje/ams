package by.anatolyloyko.ams.user.command

import by.anatolyloyko.ams.user.EXTERNAL_USER_ID
import by.anatolyloyko.ams.user.NEW_USER
import by.anatolyloyko.ams.user.USER_ID
import by.anatolyloyko.ams.user.action.CreateUserAction
import by.anatolyloyko.ams.user.command.input.CreateUserCommandInput
import by.anatolyloyko.ams.user.model.User
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.WithAssertions
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class CreateUserCommandHandlerTest : WithAssertions {
    private val createUserAction = mockk<CreateUserAction> {
        every { this@mockk(any<User>(), any()) } returns USER_ID
    }

    private val handler = CreateUserCommandHandler(createUserAction)

    private val command = CreateUserCommand(
        input = CreateUserCommandInput(
            user = NEW_USER,
            externalId = EXTERNAL_USER_ID,
        ),
    )

    @Test
    @Disabled
    fun `must invoke the action`() {
        val result = handler.handle(command)

        assertThat(result).isEqualTo(USER_ID)
        verify(exactly = 1) {
            createUserAction(
                user = command.input.user,
                externalId = EXTERNAL_USER_ID,
            )
        }
    }

    @Test
    fun `must throw not implemented error`() {
        assertThrows<NotImplementedError> { handler.handle(command) }
    }
}
