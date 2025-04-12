package by.anatolyloyko.ams.administration.role.command

import by.anatolyloyko.ams.administration.role.action.SaveRoleAction
import by.anatolyloyko.ams.common.infrastructure.service.command.BaseCommandHandler
import org.springframework.stereotype.Component

/**
 * Handles {@link SaveRoleCommand}.
 *
 * Saves a provided role.
 */
@Component
class SaveRoleCommandHandler(
    private val saveRoleAction: SaveRoleAction
) : BaseCommandHandler<SaveRoleCommand, Long>() {
    override fun handleInternal(command: SaveRoleCommand): Long = saveRoleAction(
        role = command.input.role,
        permissions = command.input.permissions
    )
}
