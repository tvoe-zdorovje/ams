package by.anatolyloyko.ams.auth.user.finder

import by.anatolyloyko.ams.auth.IDP_USER_ID
import by.anatolyloyko.ams.auth.USER_ID
import by.anatolyloyko.ams.auth.user.model.User
import by.anatolyloyko.ams.orm.jooq.schemas.users.tables.references.USER
import org.assertj.core.api.WithAssertions
import org.jooq.DSLContext
import org.jooq.SQLDialect
import org.jooq.conf.ParamType
import org.jooq.impl.DSL
import org.jooq.tools.jdbc.MockConnection
import org.jooq.tools.jdbc.MockExecuteContext
import org.jooq.tools.jdbc.MockResult
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class JooqUserFinderTest : WithAssertions {
    private val dslContext: DSLContext = DSL.using(
        MockConnection { ctx -> mockResult(ctx) },
        SQLDialect.POSTGRES,
    )

    private val finder = JooqUserFinder(dslContext)

    private var hasRecord = true

    private lateinit var actualSQL: String

    private lateinit var actualBindings: List<Any?>

    @BeforeEach
    fun beforeEach() {
        hasRecord = true
    }

    @Test
    fun `must find user by idp uuid`() {
        val result = finder.byIdpUUID(IDP_USER_ID)

        val expectedQuery = DSL
            .select(USER.ID)
            .from(USER)
            .where(USER.IDP_UUID.eq(IDP_USER_ID))

        assertThat(actualSQL).isEqualTo(expectedQuery.getSQL(ParamType.INDEXED))
        assertThat(actualBindings).containsExactly(IDP_USER_ID)
        assertThat(result).isEqualTo(
            User(
                id = USER_ID,
                idpUUID = IDP_USER_ID,
            )
        )
    }

    @Test
    fun `must return null if no user found`() {
        hasRecord = false

        val result = finder.byIdpUUID(IDP_USER_ID)

        assertThat(result).isNull()
    }

    private fun mockResult(ctx: MockExecuteContext): Array<MockResult> {
        actualSQL = ctx.sql()
        actualBindings = ctx.bindings().toList()

        val result = dslContext.newResult(USER.ID)
        if (hasRecord) {
            result += dslContext.newRecord(USER.ID).apply {
                this[USER.ID] = USER_ID
            }
        }

        return arrayOf(MockResult(result.size, result))
    }
}
