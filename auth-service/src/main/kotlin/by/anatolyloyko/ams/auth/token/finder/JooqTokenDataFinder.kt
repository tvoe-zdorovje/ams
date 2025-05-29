package by.anatolyloyko.ams.auth.token.finder

import by.anatolyloyko.ams.auth.token.model.Permission
import by.anatolyloyko.ams.auth.token.model.TokenData
import by.anatolyloyko.ams.orm.jooq.schemas.administration.tables.references.BRAND_ROLES
import by.anatolyloyko.ams.orm.jooq.schemas.administration.tables.references.PERMISSION
import by.anatolyloyko.ams.orm.jooq.schemas.administration.tables.references.ROLE_PERMISSIONS
import by.anatolyloyko.ams.orm.jooq.schemas.administration.tables.references.STUDIO_ROLES
import by.anatolyloyko.ams.orm.jooq.schemas.administration.tables.references.USER
import by.anatolyloyko.ams.orm.jooq.schemas.administration.tables.references.USER_ROLES
import org.jooq.DSLContext
import org.jooq.Record
import org.jooq.Result
import org.springframework.stereotype.Component

val PERMISSION_MAPPER: (Result<out Record>) -> Map<Long, List<Permission>> = { result ->
    result
        .ifEmpty { throw NoSuchElementException("No user found") }
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

@Component
class JooqTokenDataFinder(
    private val dslContext: DSLContext
) : TokenDataFinder {
    override fun findByUserId(userId: Long): TokenData? = TokenData(
        userId = userId,
        permissions = findPermissionsByUserId(userId)
    )

    private fun findPermissionsByUserId(userId: Long): Map<Long, List<Permission>> {
        val result = dslContext
            .select(
                PERMISSION.ID,
                PERMISSION.NAME,
                BRAND_ROLES.BRAND_ID,
                STUDIO_ROLES.STUDIO_ID
            )
            .from(USER)
            .leftOuterJoin(USER_ROLES)
            .on(USER.ID.eq(USER_ROLES.USER_ID))
            .leftOuterJoin(BRAND_ROLES)
            .on(USER_ROLES.ROLE_ID.eq(USER_ROLES.ROLE_ID))
            .leftOuterJoin(STUDIO_ROLES)
            .on(STUDIO_ROLES.ROLE_ID.eq(USER_ROLES.ROLE_ID))
            .leftOuterJoin(ROLE_PERMISSIONS)
            .on(ROLE_PERMISSIONS.ROLE_ID.eq(USER_ROLES.ROLE_ID))
            .leftOuterJoin(PERMISSION)
            .on(PERMISSION.ID.eq(ROLE_PERMISSIONS.PERMISSION_ID))
            .where(USER.ID.eq(userId))
            .fetch()

        return PERMISSION_MAPPER(result)
    }
}
