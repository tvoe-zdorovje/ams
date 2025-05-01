package by.anatolyloyko.ams.administration.user.action

import by.anatolyloyko.ams.orm.jooq.schemas.routines.references.deleteUserRoles
import org.jooq.DSLContext
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Transactional
class JooqUnassignRolesAction(
    private val dslContext: DSLContext
) : UnassignRolesAction {
    override fun invoke(userId: Long, roles: List<Long>) {
        deleteUserRoles(
            configuration = dslContext.configuration(),
            iUserId = userId,
            iRoles = roles.toTypedArray()
        )
    }
}
