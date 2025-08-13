package by.anatolyloyko.ams.administration.role.finder

import by.anatolyloyko.ams.administration.JooqTest
import by.anatolyloyko.ams.administration.ROLE
import by.anatolyloyko.ams.administration.ROUTINES_REFERENCE
import by.anatolyloyko.ams.administration.STUDIO_ID
import by.anatolyloyko.ams.orm.jooq.schemas.administration.routines.references.assertRolesBelongTo
import by.anatolyloyko.ams.orm.jooq.schemas.administration.tables.records.RoleRecord
import io.mockk.every
import io.mockk.mockkStatic
import org.jooq.exception.DataAccessException
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class JooqRoleFinderTest : JooqTest() {
    @Autowired
    private lateinit var finder: JooqRoleFinder

    @BeforeAll
    fun beforeAll() {
        dslContext.executeInsert(RoleRecord(ROLE.id!!, ROLE.name, ROLE.description))
    }

    @Test
    fun `must return null if no roles found by id`() = mockkStatic(ROUTINES_REFERENCE) {
        every { assertRolesBelongTo(any(), any(), any()) } returns Unit

        val result = finder.findById(Long.MIN_VALUE, STUDIO_ID)

        assertThat(result).isNull()
    }

    @Test
    fun `must return null if role does not belong to organization`() = mockkStatic(ROUTINES_REFERENCE) {
        every { assertRolesBelongTo(any(), any(), any()) } throws DataAccessException("msg")

        val result = finder.findById(Long.MIN_VALUE, STUDIO_ID)

        assertThat(result).isNull()
    }

    @Test
    fun `must find and return the role by id`() = mockkStatic(ROUTINES_REFERENCE) {
        every { assertRolesBelongTo(any(), any(), any()) } returns Unit

        val result = finder.findById(ROLE.id!!, STUDIO_ID)

        assertThat(result).isEqualTo(ROLE)
    }
}
