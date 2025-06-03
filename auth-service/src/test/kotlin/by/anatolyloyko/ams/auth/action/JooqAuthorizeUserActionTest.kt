package by.anatolyloyko.ams.auth.action

import by.anatolyloyko.ams.auth.action.util.PasswordVerifier
import by.anatolyloyko.ams.auth.exception.ForbiddenException
import by.anatolyloyko.ams.orm.jooq.schemas.users.tables.references.USER
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import org.assertj.core.api.WithAssertions
import org.jooq.DSLContext
import org.jooq.Record
import org.jooq.ResultQuery
import org.jooq.SQLDialect
import org.jooq.conf.ParamType
import org.jooq.impl.DefaultDSLContext
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import javax.sql.DataSource
import by.anatolyloyko.ams.orm.jooq.schemas.users.tables.references.USER_PASSWORD as USER_PASSWORD_TABLE

private const val USER_ID = 100000001413121100

private const val PHONE_NUMBER = "+375297671245"

private val USER_PASSWORD = "tankist_1998".toCharArray()

private const val USER_PASSWORD_HASH = "hashed_tankist_1998"

private val TABLE = selectQuery().asTable()

class JooqAuthorizeUserActionTest : WithAssertions {
    private val dslContext = spyk(DefaultDSLContext(mockk<DataSource>(), SQLDialect.POSTGRES))

    private val passwordVerifier = mockk<PasswordVerifier>(relaxed = true) {
        every { verify(USER_PASSWORD, USER_PASSWORD_HASH) } returns true
    }

    private val action = JooqAuthorizeUserAction(dslContext, passwordVerifier)

    @BeforeEach
    fun beforeEach() {
        dslContext.mockResult()
    }

    @Test
    fun `must find user ID and password hash by phone number`() {
        action(PHONE_NUMBER, USER_PASSWORD)

        val expectedQuery = selectQuery().where(USER.PHONE_NUMBER.eq(PHONE_NUMBER))
        verify(exactly = 1) {
            dslContext.fetchOne(
                match<ResultQuery<Record>> {
                    val inlined = ParamType.INLINED
                    it.getSQL(inlined) == expectedQuery.getSQL(inlined)
                }
            )
        }
    }

    @Test
    fun `must throw exception when no record found`() {
        every { dslContext.fetchOne(any<ResultQuery<*>>()) } returns null

        assertThatThrownBy { action(PHONE_NUMBER, USER_PASSWORD) }
            .isOfAnyClassIn(ForbiddenException::class.java)
            .hasMessage("Unknown phone number $PHONE_NUMBER")
    }

    @Test
    fun `must return user ID`() {
        val result = action(PHONE_NUMBER, USER_PASSWORD)

        assertThat(result).isEqualTo(USER_ID)
    }

    @Test
    fun `must verify password`() {
        action(PHONE_NUMBER, USER_PASSWORD)

        verify { passwordVerifier.verify(USER_PASSWORD, USER_PASSWORD_HASH) }
    }

    @Test
    fun `must throw exception when password does not match`() {
        dslContext.mockResult(hashedPassword = "wrong hash")

        assertThatThrownBy { action(PHONE_NUMBER, USER_PASSWORD) }
            .isOfAnyClassIn(ForbiddenException::class.java)
            .hasMessage("Wrong password")
    }

    private fun DSLContext.mockResult(
        userId: Long = USER_ID,
        hashedPassword: String = USER_PASSWORD_HASH
    ) = every { fetchOne(any<ResultQuery<*>>()) } returns newRecord(TABLE).apply {
        this[USER.ID] = userId
        this[USER_PASSWORD_TABLE.PASSWORD] = hashedPassword
    }
}
