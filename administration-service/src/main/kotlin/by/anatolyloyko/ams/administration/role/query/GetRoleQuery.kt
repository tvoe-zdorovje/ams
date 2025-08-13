package by.anatolyloyko.ams.administration.role.query

import by.anatolyloyko.ams.administration.role.model.Role
import by.anatolyloyko.ams.administration.role.query.input.GetRoleQueryInput
import by.anatolyloyko.ams.common.infrastructure.service.query.BaseQuery

/**
 * A query to retrieve a role [Role] by criteria [GetRoleQueryInput].
 *
 * @param input contains necessary data to look up a role.
 */
class GetRoleQuery(
    input: GetRoleQueryInput
) : BaseQuery<GetRoleQueryInput, Role?>(input)
