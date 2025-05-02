package by.anatolyloyko.ams.studio.finder

import by.anatolyloyko.ams.orm.jooq.schemas.tables.records.StudioRecord
import by.anatolyloyko.ams.studio.JooqTest
import by.anatolyloyko.ams.studio.STUDIO
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class JooqStudioFinderTest : JooqTest() {
    @Autowired
    private lateinit var finder: JooqStudioFinder

    @BeforeAll
    fun beforeAll() {
        dslContext.executeInsert(StudioRecord(STUDIO.id!!, STUDIO.name, STUDIO.description))
    }

    @Test
    fun `must return null if no studios found by id`() {
        val result = finder.findById(Long.MIN_VALUE)

        assertThat(result).isNull()
    }

    @Test
    fun `must find and return the studio by id`() {
        val result = finder.findById(STUDIO.id!!)

        assertThat(result).isEqualTo(STUDIO)
    }
}
