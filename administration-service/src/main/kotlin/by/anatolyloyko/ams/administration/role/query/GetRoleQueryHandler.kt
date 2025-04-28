package by.anatolyloyko.ams.administration.role.query

import by.anatolyloyko.ams.administration.role.finder.RoleFinder
import by.anatolyloyko.ams.administration.role.model.Role
import by.anatolyloyko.ams.common.infrastructure.service.query.BaseQueryHandler
import org.springframework.stereotype.Component

/**
 * Handles {@link GetRoleQuery}.
 *
 * Find a role based on the provided data.
 */
@Component
class GetRoleQueryHandler(
    private val roleFinder: RoleFinder,
) : BaseQueryHandler<GetRoleQuery, Role?>() {
    override fun handleInternal(query: GetRoleQuery): Role? = roleFinder.findById(query.input)
}
