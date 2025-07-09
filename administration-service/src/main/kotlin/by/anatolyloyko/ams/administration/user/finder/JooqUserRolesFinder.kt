package by.anatolyloyko.ams.administration.user.finder

import by.anatolyloyko.ams.administration.role.model.Role
import by.anatolyloyko.ams.orm.jooq.schemas.tables.references.BRAND_ROLES
import by.anatolyloyko.ams.orm.jooq.schemas.tables.references.ROLE
import by.anatolyloyko.ams.orm.jooq.schemas.tables.references.STUDIO_ROLES
import by.anatolyloyko.ams.orm.jooq.schemas.tables.references.USER_ROLES
import by.anatolyloyko.ams.orm.jooq.util.eq
import by.anatolyloyko.ams.orm.jooq.util.or
import org.jooq.DSLContext
import org.jooq.Record
import org.springframework.stereotype.Component

private val MAPPER: (Record) -> Role = {
    Role(
        id = it[ROLE.ID],
        name = it[ROLE.NAME]!!,
        description = it[ROLE.DESCRIPTION]!!
    )
}

@Component
class JooqUserRolesFinder(
    private val dslContext: DSLContext
) : UserRolesFinder {
    override fun findByOrganizationId(userId: Long, organizationId: Long): List<Role> = dslContext
        .select(
            ROLE.ID,
            ROLE.NAME,
            ROLE.DESCRIPTION
        )
        .from(USER_ROLES)
        .leftJoin(BRAND_ROLES)
        .on(BRAND_ROLES.ROLE_ID eq USER_ROLES.ROLE_ID)
        .leftJoin(STUDIO_ROLES)
        .on(STUDIO_ROLES.ROLE_ID eq USER_ROLES.ROLE_ID)
        .leftJoin(ROLE)
        .on(ROLE.ID eq USER_ROLES.ROLE_ID)
        .where(USER_ROLES.USER_ID eq userId)
        .and((BRAND_ROLES.BRAND_ID eq organizationId) or (STUDIO_ROLES.STUDIO_ID eq organizationId))
        .map(MAPPER)
}
