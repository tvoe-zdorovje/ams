package by.anatolyloyko.ams.auth.action

import by.anatolyloyko.ams.auth.action.util.PasswordVerifier
import by.anatolyloyko.ams.auth.exception.ForbiddenException
import by.anatolyloyko.ams.orm.jooq.schemas.users.tables.references.USER
import by.anatolyloyko.ams.orm.jooq.schemas.users.tables.references.USER_PASSWORD
import org.jooq.DSLContext
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Transactional(readOnly = true)
class JooqAuthorizeUserAction(
    private val dslContext: DSLContext,
    private val passwordVerifier: PasswordVerifier
) : AuthorizeUserAction {

    override fun invoke(phoneNumber: String, password: CharArray): Long {
        val (userId, hash) = dslContext
            .select(USER.ID, USER_PASSWORD.PASSWORD)
            .from(USER)
            .leftJoin(USER_PASSWORD)
            .on(USER.ID.eq(USER_PASSWORD.USER_ID))
            .where(USER.PHONE_NUMBER.eq(phoneNumber))
            .fetchOne()
            ?.map { it[USER.ID]!! to it[USER_PASSWORD.PASSWORD]!! }
            ?: throw ForbiddenException("No user found for phone number $phoneNumber")

        val matches = passwordVerifier.verify(password, hash)
        if (matches) {
            return userId
        }

        throw ForbiddenException("Wrong password")
    }
}
