package by.anatolyloyko.ams.user.command

import by.anatolyloyko.ams.infrastructure.service.command.BaseCommandHandler
import by.anatolyloyko.ams.user.action.CreateUserAction
import org.springframework.stereotype.Component

/**
 * Handles {@link CreateUserCommand}.
 *
 * Creates a new user based on the provided data.
 */
@Component
class CreateUserCommandHandler(
    private val createUserAction: CreateUserAction
) : BaseCommandHandler<CreateUserCommand, Long>() {
    override fun handleInternal(command: CreateUserCommand): Long = createUserAction(command.input)
}
