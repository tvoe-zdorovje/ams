package by.anatolyloyko.ams.user.action

import by.anatolyloyko.ams.orm.jooq.schemas.users.routines.references.saveUser
import by.anatolyloyko.ams.user.model.User
import org.jooq.DSLContext
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Transactional
@Component
class JooqUpdateUserAction(
    private val dslContext: DSLContext
) : UpdateUserAction {
    override fun invoke(user: User): Long = saveUser(
        configuration = dslContext.configuration(),
        iId = user.id!!,
        iFirstName = user.firstName,
        iLastName = user.lastName,
        iPhoneNumber = user.phoneNumber
    ) ?: error("Could not save user $user")
}
