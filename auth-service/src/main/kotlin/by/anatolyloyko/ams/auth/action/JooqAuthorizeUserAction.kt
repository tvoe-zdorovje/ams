package by.anatolyloyko.ams.auth.action

import by.anatolyloyko.ams.auth.action.util.PasswordVerifier
import by.anatolyloyko.ams.auth.exception.ForbiddenException
import org.jooq.DSLContext

class JooqAuthorizeUserAction(
    private val dslContext: DSLContext,
    private val passwordVerifier: PasswordVerifier
) : AuthorizeUserAction {

    override fun invoke(phoneNumber: String, password: CharArray): Long {
        val userId = -1L
        val hash = "pass"
        TODO()
        if (userId == null) {
            throw ForbiddenException("Unknown phone number")
        }

        val matches = passwordVerifier.verify(password, hash)
        if (matches) {
            return userId
        }

        throw ForbiddenException("Wrong password")
    }
}
