package by.anatolyloyko.ams.administration.brand.studio.command

import by.anatolyloyko.ams.administration.brand.studio.action.CreateStudioRoleAction
import by.anatolyloyko.ams.common.infrastructure.service.command.BaseCommandHandler
import org.springframework.stereotype.Component

/**
 * Handles {@link CreateStudioRoleCommand}.
 *
 * Saves a provided role with permissions for a provided studio.
 */
@Component
class CreateStudioRoleCommandHandler(
    private val createStudioRoleAction: CreateStudioRoleAction
) : BaseCommandHandler<CreateStudioRoleCommand, Long>() {
    override fun handleInternal(command: CreateStudioRoleCommand): Long = createStudioRoleAction(
        studioId = command.input.studioId,
        role = command.input.role,
        permissions = command.input.permissions
    )
}
