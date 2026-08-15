package by.anatolyloyko.ams.auth.user.finder

import by.anatolyloyko.ams.auth.user.model.User
import by.anatolyloyko.ams.orm.jooq.schemas.users.tables.references.USER
import org.jooq.DSLContext
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component

/**
 * {@inheritDoc}
 *
 * This implementation is based on the jOOQ library.
 */
@Component
class JooqUserFinder(
    @param:Qualifier("userDslContext")
    private val dslContext: DSLContext,
) : UserFinder {
    override fun byIdpUUID(idpUUID: String): User? = dslContext
        .select(USER.ID)
        .from(USER)
        .where(USER.IDP_UUID.eq(idpUUID))
        .fetchOne {
            User(
                id = requireNotNull(it[USER.ID]),
                idpUUID = idpUUID,
            )
        }
}
