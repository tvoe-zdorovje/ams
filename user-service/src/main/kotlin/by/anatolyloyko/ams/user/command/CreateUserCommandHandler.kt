package by.anatolyloyko.ams.user.command

import by.anatolyloyko.ams.common.infrastructure.service.command.BaseCommandHandler
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
    override fun handleInternal(command: CreateUserCommand): Long {
        TODO("Not yet implemented. Must be covered with a new permisson (admin only)")
        return createUserAction(
            user = command.input.user,
            externalId = command.input.externalId,
        )
    }
}
