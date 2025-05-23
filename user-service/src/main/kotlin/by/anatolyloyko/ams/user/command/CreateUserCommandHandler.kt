package by.anatolyloyko.ams.user.command

import by.anatolyloyko.ams.common.infrastructure.service.command.BaseCommandHandler
import by.anatolyloyko.ams.user.action.CreateUserAction
import by.anatolyloyko.ams.user.password.action.HashPasswordAction
import org.springframework.stereotype.Component

/**
 * Handles {@link CreateUserCommand}.
 *
 * Creates a new user based on the provided data.
 */
@Component
class CreateUserCommandHandler(
    private val createUserAction: CreateUserAction,
    private val hashPasswordAction: HashPasswordAction
) : BaseCommandHandler<CreateUserCommand, Long>() {
    override fun handleInternal(command: CreateUserCommand): Long = createUserAction(
        user = command.input.user,
        password = hashPasswordAction(command.input.password)
    )
}
