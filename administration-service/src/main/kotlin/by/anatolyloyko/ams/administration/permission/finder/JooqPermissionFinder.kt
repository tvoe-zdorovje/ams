package by.anatolyloyko.ams.administration.permission.finder

import by.anatolyloyko.ams.administration.permission.model.Permission
import by.anatolyloyko.ams.orm.jooq.schemas.tables.references.PERMISSION
import by.anatolyloyko.ams.orm.jooq.schemas.tables.references.ROLE_PERMISSIONS
import by.anatolyloyko.ams.orm.jooq.util.eq
import org.jooq.DSLContext
import org.jooq.Record3
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

private val MAPPER: (Record3<Long?, String?, String?>) -> Permission = {
    Permission(
        id = checkNotNull(it[PERMISSION.ID]),
        name = checkNotNull(it[PERMISSION.NAME]),
        description = checkNotNull(it[PERMISSION.DESCRIPTION])
    )
}

@Component
@Transactional(readOnly = true)
class JooqPermissionFinder(
    private val dsl: DSLContext
) : PermissionFinder {
    override fun findAll(): List<Permission> = dsl
        .selectFrom(PERMISSION)
        .orderBy(PERMISSION.ID)
        .fetch(MAPPER)

    override fun findByRoleId(id: Long): List<Permission> = dsl
        .select(
            PERMISSION.ID,
            PERMISSION.NAME,
            PERMISSION.DESCRIPTION
        )
        .from(ROLE_PERMISSIONS)
        .leftJoin(PERMISSION)
        .on(ROLE_PERMISSIONS.PERMISSION_ID eq PERMISSION.ID)
        .where(ROLE_PERMISSIONS.ROLE_ID eq id)
        .orderBy(PERMISSION.ID)
        .fetch(MAPPER)
}
