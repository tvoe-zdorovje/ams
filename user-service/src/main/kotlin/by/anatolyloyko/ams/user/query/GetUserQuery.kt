package by.anatolyloyko.ams.user.query

import by.anatolyloyko.ams.infrastructure.service.query.BaseQuery
import by.anatolyloyko.ams.user.model.User

/**
 * A query to retrieve a user {@link User} by ID {@link Long}.
 *
 * @param input the ID of the user to retrieve.
 */
class GetUserQuery(
    input: Long
) : BaseQuery<Long, User?>(input)
