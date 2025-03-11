package by.anatolyloyko.ams.user.query

import by.anatolyloyko.ams.common.infrastructure.service.query.BaseQueryHandler
import by.anatolyloyko.ams.user.finder.UserFinder
import by.anatolyloyko.ams.user.model.User
import org.springframework.stereotype.Component

/**
 * Handles {@link GetUserQuery}.
 *
 * Find a user based on the provided data.
 */
@Component
class GetUserQueryHandler(
    private val userFinder: UserFinder,
) : BaseQueryHandler<GetUserQuery, User?>() {
    override fun handleInternal(query: GetUserQuery): User? = userFinder.findById(query.input)
}
