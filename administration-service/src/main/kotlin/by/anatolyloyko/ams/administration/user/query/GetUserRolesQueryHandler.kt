package by.anatolyloyko.ams.administration.user.query

import by.anatolyloyko.ams.administration.role.model.Role
import by.anatolyloyko.ams.administration.user.finder.UserRolesFinder
import by.anatolyloyko.ams.common.infrastructure.service.query.BaseQueryHandler
import org.springframework.stereotype.Component

/**
 * Handles [GetUserRolesQuery].
 *
 * Finds and returns user roles based on the provided data.
 */
@Component
class GetUserRolesQueryHandler(
    private val userRolesFinder: UserRolesFinder
) : BaseQueryHandler<GetUserRolesQuery, List<Role>>() {
    override fun handleInternal(query: GetUserRolesQuery): List<Role> = userRolesFinder.findByOrganizationId(
        userId = query.input.userId,
        organizationId = query.input.organizationId
    )
}
