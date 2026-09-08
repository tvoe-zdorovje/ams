package by.anatolyloyko.ams.auth.user.finder

import by.anatolyloyko.ams.auth.user.model.User
import by.anatolyloyko.ams.orm.jooq.schemas.users.tables.references.USER
import org.jooq.Condition
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
    override fun byId(id: Long): User? = selectUserWhere(USER.ID.eq(id))

    override fun byIdpUUID(idpUUID: String): User? = selectUserWhere(USER.IDP_UUID.eq(idpUUID))

    private fun selectUserWhere(condition: Condition) = dslContext
        .select(USER.ID, USER.IDP_UUID)
        .from(USER)
        .where(condition)
        .fetchOne {
            User(
                id = requireNotNull(it[USER.ID]),
                idpUUID = requireNotNull(it[USER.IDP_UUID]),
            )
        }
}
