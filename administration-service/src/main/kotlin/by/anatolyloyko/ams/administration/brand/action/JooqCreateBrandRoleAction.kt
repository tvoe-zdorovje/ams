package by.anatolyloyko.ams.administration.brand.action

import by.anatolyloyko.ams.administration.role.model.Role
import by.anatolyloyko.ams.orm.jooq.schemas.routines.references.createBrandRole
import org.jooq.DSLContext
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Transactional
class JooqCreateBrandRoleAction(
    private val dslContext: DSLContext
) : CreateBrandRoleAction {
    override fun invoke(
        brandId: Long,
        role: Role,
        permissions: List<Long>
    ) = createBrandRole(
        configuration = dslContext.configuration(),
        iBrandId = brandId,
        iName = role.name,
        iDescription = role.description,
        iPermissions = permissions.toTypedArray()
    ) ?: error("Could not create a new role $role with permissions $permissions for brand $brandId")
}
