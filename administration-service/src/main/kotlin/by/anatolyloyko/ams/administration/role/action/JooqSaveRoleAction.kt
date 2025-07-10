package by.anatolyloyko.ams.administration.role.action

import by.anatolyloyko.ams.administration.role.model.Role
import by.anatolyloyko.ams.orm.jooq.schemas.administration.routines.references.saveRole
import org.jooq.DSLContext
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Transactional
internal class JooqSaveRoleAction(
    private val dslContext: DSLContext
) : SaveRoleAction {
    override fun invoke(role: Role, permissions: List<Long>): Long = saveRole(
        configuration = dslContext.configuration(),
        iId = role.id,
        iName = role.name,
        iDescription = role.description,
        iPermissions = permissions.toTypedArray()
    ) ?: error("Could not save role $role")
}
