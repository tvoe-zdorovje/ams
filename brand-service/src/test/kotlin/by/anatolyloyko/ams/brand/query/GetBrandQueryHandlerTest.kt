package by.anatolyloyko.ams.brand.query

import by.anatolyloyko.ams.brand.BRAND
import by.anatolyloyko.ams.brand.BRAND_ID
import by.anatolyloyko.ams.brand.finder.BrandFinder
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.WithAssertions
import org.junit.jupiter.api.Test

class GetBrandQueryHandlerTest : WithAssertions {
    private val brandFinder = mockk<BrandFinder> {
        every { findById(BRAND_ID) } returns BRAND
    }
    private val handler = GetBrandQueryHandler(brandFinder)

    private val query = GetBrandQuery(input = BRAND_ID)

    @Test
    fun `must invoke the action`() {
        val result = handler.handle(query)

        assertThat(result).isEqualTo(BRAND)
        verify(exactly = 1) { brandFinder.findById(query.input) }
    }
}
