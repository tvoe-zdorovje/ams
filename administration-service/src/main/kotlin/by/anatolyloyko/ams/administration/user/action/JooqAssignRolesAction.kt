package by.anatolyloyko.ams.administration.user.action

import by.anatolyloyko.ams.orm.jooq.schemas.administration.routines.references.addUserRoles
import org.jooq.DSLContext
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Transactional
class JooqAssignRolesAction(
    private val dslContext: DSLContext,
) : AssignRolesAction {
    override fun invoke(
        userId: Long,
        organizationId: Long,
        roles: List<Long>
    ) {
        addUserRoles(
            configuration = dslContext.configuration(),
            iUserId = userId,
            iOrganizationId = organizationId,
            iRoles = roles.toTypedArray()
        )
    }
}
