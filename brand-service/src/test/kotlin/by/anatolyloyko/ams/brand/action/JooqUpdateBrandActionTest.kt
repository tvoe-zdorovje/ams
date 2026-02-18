package by.anatolyloyko.ams.brand.action

import by.anatolyloyko.ams.brand.BRAND
import by.anatolyloyko.ams.brand.BRAND_ID
import by.anatolyloyko.ams.brand.ROUTINES_REFERENCE
import by.anatolyloyko.ams.brand.USER_ID
import by.anatolyloyko.ams.orm.jooq.schemas.brands.routines.references.updateBrand
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import org.assertj.core.api.WithAssertions
import org.jooq.DSLContext
import org.junit.jupiter.api.Test

class JooqUpdateBrandActionTest : WithAssertions {
    private val dslContext = mockk<DSLContext>(relaxed = true)

    private val action = JooqUpdateBrandAction(dslContext)

    @Test
    fun `must call update brand routine`() = mockkStatic(ROUTINES_REFERENCE) {
        every { updateBrand(any(), any(), any(), any()) } returns BRAND_ID

        val result = action(BRAND)

        assertThat(result).isEqualTo(BRAND_ID)
        verify(exactly = 1) { updateBrand(any(), BRAND.id, BRAND.name, BRAND.description) }
    }

    @Test
    fun `must throw exception when update brand routine invocation result is null`() = mockkStatic(ROUTINES_REFERENCE) {
        every { updateBrand(any(), any(), any(), any()) } returns null

        assertThatThrownBy { action(BRAND) }
            .isOfAnyClassIn(IllegalStateException::class.java)
            .hasMessage("Could not save the brand $BRAND")

        verify(exactly = 1) { updateBrand(any(), BRAND.id, BRAND.name, BRAND.description) }
    }
}