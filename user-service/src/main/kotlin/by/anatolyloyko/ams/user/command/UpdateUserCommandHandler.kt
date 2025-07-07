package by.anatolyloyko.ams.user.command

import by.anatolyloyko.ams.common.infrastructure.service.command.BaseCommandHandler
import by.anatolyloyko.ams.user.action.UpdateUserAction
import org.springframework.stereotype.Component

/**
 * Handles {@link UpdateUserCommand}.
 *
 * Updates a user based on the provided data.
 */
@Component
class UpdateUserCommandHandler(
    private val updateUserAction: UpdateUserAction
) : BaseCommandHandler<UpdateUserCommand, Long>() {
    override fun handleInternal(command: UpdateUserCommand): Long = updateUserAction(command.input.user)
}
