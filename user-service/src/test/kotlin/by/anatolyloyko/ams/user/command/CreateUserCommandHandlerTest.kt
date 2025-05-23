package by.anatolyloyko.ams.user.command

import by.anatolyloyko.ams.user.NEW_USER
import by.anatolyloyko.ams.user.USER_ID
import by.anatolyloyko.ams.user.USER_PASSWORD
import by.anatolyloyko.ams.user.action.CreateUserAction
import by.anatolyloyko.ams.user.command.input.CreateUserCommandInput
import by.anatolyloyko.ams.user.model.User
import by.anatolyloyko.ams.user.password.action.HashPasswordAction
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.WithAssertions
import org.junit.jupiter.api.Test

private const val HASHED_PASSWORD = "hashedPassword"

class CreateUserCommandHandlerTest : WithAssertions {
    private val createUserAction = mockk<CreateUserAction> {
        every { this@mockk(any<User>(), any()) } returns USER_ID
    }

    private val hashPasswordAction = mockk<HashPasswordAction> {
        every { this@mockk(any()) } returns HASHED_PASSWORD
    }

    private val handler = CreateUserCommandHandler(createUserAction, hashPasswordAction)

    private val command = CreateUserCommand(
        input = CreateUserCommandInput(
            user = NEW_USER,
            password = USER_PASSWORD.toCharArray(),
        ),
    )

    @Test
    fun `must invoke the action`() {
        val result = handler.handle(command)

        assertThat(result).isEqualTo(USER_ID)
        verify(exactly = 1) {
            hashPasswordAction(command.input.password)
            createUserAction(
                user = command.input.user,
                password = HASHED_PASSWORD,
            )
        }
    }
}
