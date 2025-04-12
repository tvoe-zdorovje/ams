package by.anatolyloyko.ams.administration.role.query

import by.anatolyloyko.ams.administration.role.model.Role
import by.anatolyloyko.ams.common.infrastructure.service.query.BaseQuery

/**
 * A query to retrieve a role {@link Role} by ID {@link Long}.
 *
 * @param input the ID of the role to retrieve.
 */
class GetRoleQuery(
    input: Long
) : BaseQuery<Long, Role?>(input)
