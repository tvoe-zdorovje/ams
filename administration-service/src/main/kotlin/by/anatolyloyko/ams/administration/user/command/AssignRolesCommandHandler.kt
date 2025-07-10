package by.anatolyloyko.ams.administration.user.command

import by.anatolyloyko.ams.administration.user.action.AssignRolesAction
import by.anatolyloyko.ams.common.infrastructure.service.command.BaseCommandHandler
import org.springframework.stereotype.Component

/**
 * Handles {@link AssignRolesCommand}.
 *
 * Assigns provided roles to a user.
 */
@Component
class AssignRolesCommandHandler(
    private val assignRolesAction: AssignRolesAction
) : BaseCommandHandler<AssignRolesCommand, Unit>() {
    override fun handleInternal(command: AssignRolesCommand) = assignRolesAction(
        userId = command.input.userId,
        organizationId = command.input.organizationId,
        roles = command.input.roles
    )
}
