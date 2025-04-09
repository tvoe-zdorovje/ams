package by.anatolyloyko.ams.brand.finder

import by.anatolyloyko.ams.brand.BRAND
import by.anatolyloyko.ams.brand.JooqTest
import by.anatolyloyko.ams.orm.jooq.schemas.tables.records.BrandRecord
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class JooqBrandFinderTest : JooqTest() {
    @Autowired
    private lateinit var finder: JooqBrandFinder

    @BeforeAll
    fun beforeAll() {
        dslContext.executeInsert(BrandRecord(BRAND.id!!, BRAND.name, BRAND.description))
    }

    @Test
    fun `must return null if no brands found by id`() {
        val result = finder.findById(Long.MIN_VALUE)

        assertThat(result).isNull()
    }

    @Test
    fun `must find and return the brand by id`() {
        val result = finder.findById(BRAND.id!!)

        assertThat(result).isEqualTo(BRAND)
    }
}
