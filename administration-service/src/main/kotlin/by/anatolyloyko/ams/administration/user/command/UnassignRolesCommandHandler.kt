package by.anatolyloyko.ams.administration.user.command

import by.anatolyloyko.ams.administration.user.action.UnassignRolesAction
import by.anatolyloyko.ams.common.infrastructure.service.command.BaseCommandHandler
import org.springframework.stereotype.Component

/**
 * Handles {@link AssignRolesCommand}.
 *
 * Unassigns provided roles from a user.
 */
@Component
class UnassignRolesCommandHandler(
    private val unassignRolesAction: UnassignRolesAction
) : BaseCommandHandler<UnassignRolesCommand, Unit>() {
    override fun handleInternal(command: UnassignRolesCommand) = unassignRolesAction(
        userId = command.input.userId,
        organizationId = command.input.organizationId,
        roles = command.input.roles
    )
}
