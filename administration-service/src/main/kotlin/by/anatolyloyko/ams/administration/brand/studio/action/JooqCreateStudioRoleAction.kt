package by.anatolyloyko.ams.administration.brand.studio.action

import by.anatolyloyko.ams.administration.role.model.Role
import by.anatolyloyko.ams.orm.jooq.schemas.routines.references.createStudioRole
import org.jooq.DSLContext
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Transactional
class JooqCreateStudioRoleAction(
    private val dslContext: DSLContext
) : CreateStudioRoleAction {
    override fun invoke(
        studioId: Long,
        role: Role,
        permissions: List<Long>
    ): Long = createStudioRole(
        configuration = dslContext.configuration(),
        iStudioId = studioId,
        iName = role.name,
        iDescription = role.description,
        iPermissions = permissions.toTypedArray()
    ) ?: error("Could not create a new role $role with permissions $permissions for studio $studioId")
}
