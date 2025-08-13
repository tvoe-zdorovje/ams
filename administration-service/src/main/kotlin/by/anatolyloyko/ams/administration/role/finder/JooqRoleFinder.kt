package by.anatolyloyko.ams.administration.role.finder

import by.anatolyloyko.ams.administration.role.model.Role
import by.anatolyloyko.ams.common.infrastructure.logging.log
import by.anatolyloyko.ams.orm.jooq.schemas.administration.routines.references.assertRolesBelongTo
import by.anatolyloyko.ams.orm.jooq.schemas.administration.tables.records.RoleRecord
import by.anatolyloyko.ams.orm.jooq.schemas.administration.tables.references.ROLE
import by.anatolyloyko.ams.orm.jooq.util.eq
import org.jooq.DSLContext
import org.jooq.exception.DataAccessException
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

private val MAPPER: (RoleRecord) -> Role = {
    Role(
        id = it.id,
        name = it.name,
        description = it.description
    )
}

@Component
@Transactional(readOnly = true)
class JooqRoleFinder(
    private val dsl: DSLContext
) : RoleFinder {
    override fun findById(roleId: Long, organizationId: Long): Role? = try {
        assertRolesBelongTo(
            configuration = dsl.configuration(),
            iOrganizationId = organizationId,
            iRoles = arrayOf(roleId)
        )

        dsl
            .selectFrom(ROLE)
            .where(ROLE.ID eq roleId)
            .fetchOne(MAPPER)
    } catch (ex: DataAccessException) {
        log.warn("Error fetching role {}", ex.message)
        null
    }
}
