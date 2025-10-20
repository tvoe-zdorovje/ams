package by.anatolyloyko.ams.common.infrastructure.graphql.auth.model

private typealias OrganizationId = Long

/**
 * Represents the authenticated user performing a request with its token data.
 */
class LoggedUserTokenData(
    userId: Long,
    permissions: Map<OrganizationId, Set<Permission>>
) : LoggedUser(userId) {
    val permissions: Map<OrganizationId, Set<String>> = permissions.mapValues {
        it.value.map(Permission::name).toSet()
    }

    data class Permission(val id: Long, val name: String)
}
