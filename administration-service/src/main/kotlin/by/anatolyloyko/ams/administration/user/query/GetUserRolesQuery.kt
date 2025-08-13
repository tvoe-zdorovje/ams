package by.anatolyloyko.ams.administration.user.query

import by.anatolyloyko.ams.administration.role.model.Role
import by.anatolyloyko.ams.administration.user.query.input.GetUserRolesQueryInput
import by.anatolyloyko.ams.common.infrastructure.service.query.BaseQuery

/**
 * A query to retrieve user roles [Role] by user ID and organization ID.
 *
 * @param input contains the user ID and the organization (brand/studio) ID
 */
class GetUserRolesQuery(
    input: GetUserRolesQueryInput
) : BaseQuery<GetUserRolesQueryInput, List<Role>>(input)
