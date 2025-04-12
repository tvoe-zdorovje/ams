package by.anatolyloyko.ams.administration.role.finder

import by.anatolyloyko.ams.administration.JooqTest
import by.anatolyloyko.ams.administration.ROLE
import by.anatolyloyko.ams.orm.jooq.schemas.tables.records.RoleRecord
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
    fun `must return null if no roles found by id`() {
        val result = finder.findById(Long.MIN_VALUE)

        assertThat(result).isNull()
    }

    @Test
    fun `must find and return the role by id`() {
        val result = finder.findById(ROLE.id!!)

        assertThat(result).isEqualTo(ROLE)
    }
}
