package by.anatolyloyko.ams.administration.brand.command

import by.anatolyloyko.ams.administration.brand.action.CreateBrandRoleAction
import by.anatolyloyko.ams.common.infrastructure.service.command.BaseCommandHandler
import org.springframework.stereotype.Component

/**
 * Handles {@link CreateBrandRoleCommand}.
 *
 * Saves a provided role with permissions for a provided brand.
 */
@Component
class CreateBrandRoleCommandHandler(
    private val createBrandRoleAction: CreateBrandRoleAction
) : BaseCommandHandler<CreateBrandRoleCommand, Long>() {
    override fun handleInternal(command: CreateBrandRoleCommand): Long = createBrandRoleAction(
        brandId = command.input.brandId,
        role = command.input.role,
        permissions = command.input.permissions
    )
}
