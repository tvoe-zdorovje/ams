package by.anatolyloyko.ams.auth.action

import by.anatolyloyko.ams.auth.action.util.PasswordVerifier
import by.anatolyloyko.ams.auth.exception.ForbiddenException
import by.anatolyloyko.ams.orm.jooq.schemas.users.tables.references.USER
import by.anatolyloyko.ams.orm.jooq.schemas.users.tables.references.USER_PASSWORD
import org.jooq.DSLContext
import org.jooq.impl.DSL
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

internal fun selectQuery() = DSL
    .select(USER.ID, USER_PASSWORD.PASSWORD)
    .from(USER)
    .leftJoin(USER_PASSWORD)
    .on(USER.ID.eq(USER_PASSWORD.USER_ID))

/**
 * {@inheritDoc}
 *
 * This implementation is based on the jOOQ library.
 */
@Component
@Transactional("userTransactionManager", readOnly = true)
class JooqAuthorizeUserAction(
    @Qualifier("userDslContext")
    private val dslContext: DSLContext,
    private val passwordVerifier: PasswordVerifier
) : AuthorizeUserAction {
    /**
     * {@inheritDoc}
     */
    override fun invoke(phoneNumber: String, password: CharArray): Long {
        val query = selectQuery().where(USER.PHONE_NUMBER.eq(phoneNumber))
        val (userId, hash) = dslContext
            .fetchOne(query)
            ?.map { it[USER.ID]!! to it[USER_PASSWORD.PASSWORD]!! }
            ?: throw ForbiddenException("Unknown phone number $phoneNumber")

        val matches = passwordVerifier.verify(password, hash)
        if (matches) {
            return userId
        }

        throw ForbiddenException("Wrong password")
    }
}
