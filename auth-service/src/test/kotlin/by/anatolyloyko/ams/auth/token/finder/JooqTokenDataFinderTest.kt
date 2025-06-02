package by.anatolyloyko.ams.auth.token.finder

import by.anatolyloyko.ams.auth.token.model.Permission
import by.anatolyloyko.ams.auth.token.model.TokenData
import by.anatolyloyko.ams.orm.jooq.schemas.administration.tables.references.BRAND_ROLES
import by.anatolyloyko.ams.orm.jooq.schemas.administration.tables.references.PERMISSION
import by.anatolyloyko.ams.orm.jooq.schemas.administration.tables.references.STUDIO_ROLES
import by.anatolyloyko.ams.orm.jooq.schemas.administration.tables.references.USER_ROLES
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

private const val USER_ID = 1000000001413121100

private val TABLE = selectQuery().asTable()

class JooqTokenDataFinderTest : WithAssertions {
    private val dslContext = spyk(DefaultDSLContext(mockk<DataSource>(), SQLDialect.POSTGRES))

    private val finder = JooqTokenDataFinder(dslContext)

    @BeforeEach
    fun beforeEach() {
        dslContext.mockResult(emptyMap())
    }

    @Test
    fun `must find permissions by user ID`() {
        finder.findByUserId(USER_ID)

        val expectedQuery = selectQuery().where(USER_ROLES.USER_ID.eq(USER_ID))
        verify(exactly = 1) {
            dslContext.fetch(
                match<ResultQuery<Record>> {
                    val inlined = ParamType.INLINED
                    it.getSQL(inlined) == expectedQuery.getSQL(inlined)
                }
            )
        }
    }

    @Test
    fun `must map and return token data`() {
        val queryResult = mapOf(
            Permission(1L, "brand-studio-permission") to (101L to 1001L),
            Permission(2L, "brand-permission") to (101L to null),
            Permission(3L, "studio-permission") to (null to 1001L),
            Permission(3L, "magic-permission") to (null to null),
            Permission(-1L, "") to (null to null),
        )
        dslContext.mockResult(queryResult)
        val expected = TokenData(
            userId = USER_ID,
            permissions = queryResult.flatMap { entry ->
                listOfNotNull(entry.value.first, entry.value.second)
                    .map { it to entry.key }
            }
                .groupBy({ it.first }, { it.second })
        )

        val result = finder.findByUserId(expected.userId)

        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `must map and return token data when no permissions`() {
        val expected = TokenData(
            userId = USER_ID,
            permissions = emptyMap()
        )

        val result = finder.findByUserId(expected.userId)

        assertThat(result).isEqualTo(expected)
    }

    /**
     * Mocks a result of a database query
     *
     * @param permissions is a map with the following structure: Map<Permission, Pair<BrandId, StudioId>>
     */
    private fun DSLContext.mockResult(permissions: Map<Permission, Pair<Long?, Long?>>) = every {
        fetch(any<ResultQuery<*>>())
    } returns newResult(TABLE).apply {
        this += permissions.map { permission ->
            newRecord(TABLE).apply {
                this[PERMISSION.ID] = permission.key.id.let { if (it < 0) null else it }
                this[PERMISSION.NAME] = permission.key.name.ifEmpty { null }
                this[BRAND_ROLES.BRAND_ID] = permission.value.first
                this[STUDIO_ROLES.STUDIO_ID] = permission.value.second
            }
        }
    }
}
