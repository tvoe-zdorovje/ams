package by.anatolyloyko.ams.auth.token.finder

import by.anatolyloyko.ams.auth.token.model.Permission
import by.anatolyloyko.ams.auth.token.model.TokenData
import by.anatolyloyko.ams.orm.jooq.schemas.administration.tables.references.BRAND_ROLES
import by.anatolyloyko.ams.orm.jooq.schemas.administration.tables.references.PERMISSION
import by.anatolyloyko.ams.orm.jooq.schemas.administration.tables.references.ROLE_PERMISSIONS
import by.anatolyloyko.ams.orm.jooq.schemas.administration.tables.references.STUDIO_ROLES
import by.anatolyloyko.ams.orm.jooq.schemas.administration.tables.references.USER_ROLES
import org.jooq.DSLContext
import org.jooq.Record
import org.jooq.Result
import org.jooq.impl.DSL
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

internal val PERMISSION_MAPPER: (Result<out Record>) -> Map<Long, List<Permission>> = { result ->
    result
        .flatMap {
            val permissionId = it[PERMISSION.ID]
            val permissionName = it[PERMISSION.NAME]

            if (permissionId == null || permissionName == null) {
                return@flatMap emptyList()
            }

            val permission = Permission(permissionId, permissionName)
            listOf(
                it[BRAND_ROLES.BRAND_ID] to permission,
                it[STUDIO_ROLES.STUDIO_ID] to permission
            )
        }
        .filterNot { it.first == null }
        .distinct()
        .groupBy({ it.first!! }, { it.second })
}

internal fun selectQuery() = DSL
    .select(
        PERMISSION.ID,
        PERMISSION.NAME,
        BRAND_ROLES.BRAND_ID,
        STUDIO_ROLES.STUDIO_ID
    )
    .from(USER_ROLES)
    .leftOuterJoin(BRAND_ROLES)
    .on(BRAND_ROLES.ROLE_ID.eq(USER_ROLES.ROLE_ID))
    .leftOuterJoin(STUDIO_ROLES)
    .on(STUDIO_ROLES.ROLE_ID.eq(USER_ROLES.ROLE_ID))
    .leftOuterJoin(ROLE_PERMISSIONS)
    .on(ROLE_PERMISSIONS.ROLE_ID.eq(USER_ROLES.ROLE_ID))
    .leftOuterJoin(PERMISSION)
    .on(PERMISSION.ID.eq(ROLE_PERMISSIONS.PERMISSION_ID))

/**
 * {@inheritDoc}
 *
 * This implementation is based on the jOOQ library.
 */
@Component
@Transactional(readOnly = true)
class JooqTokenDataFinder(
    private val dslContext: DSLContext
) : TokenDataFinder {
    /**
     * {@inheritDoc}
     */
    override fun findByUserId(userId: Long): TokenData = TokenData(
        userId = userId,
        permissions = findPermissionsByUserId(userId)
    )

    private fun findPermissionsByUserId(userId: Long): Map<Long, List<Permission>> {
        val query = selectQuery().where(USER_ROLES.USER_ID.eq(userId))
        val result = dslContext.fetch(query)

        return PERMISSION_MAPPER(result)
    }
}
