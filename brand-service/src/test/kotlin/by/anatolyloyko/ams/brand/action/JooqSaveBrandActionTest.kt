package by.anatolyloyko.ams.brand.action

import by.anatolyloyko.ams.brand.BRAND
import by.anatolyloyko.ams.brand.BRAND_ID
import by.anatolyloyko.ams.brand.NEW_BRAND
import by.anatolyloyko.ams.brand.ROUTINES_REFERENCE
import by.anatolyloyko.ams.brand.USER_ID
import by.anatolyloyko.ams.orm.jooq.schemas.brands.routines.references.createBrand
import by.anatolyloyko.ams.orm.jooq.schemas.brands.routines.references.updateBrand
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import org.assertj.core.api.WithAssertions
import org.jooq.DSLContext
import org.junit.jupiter.api.Test

class JooqSaveBrandActionTest : WithAssertions {
    private val dslContext = mockk<DSLContext>(relaxed = true)

    private val action = JooqSaveBrandAction(dslContext)

    @Test
    fun `must call create brand routine and return ID if brand ID is not null`() = mockkStatic(ROUTINES_REFERENCE) {
        every { createBrand(any(), any(), any(), any()) } returns BRAND_ID

        val result = action(NEW_BRAND, USER_ID)

        assertThat(result).isEqualTo(BRAND_ID)
        verify(exactly = 1) { createBrand(any(), NEW_BRAND.name, NEW_BRAND.description, USER_ID) }
    }


    @Test
    fun `must throw exception when create brand routine invocation result is null`() = mockkStatic(ROUTINES_REFERENCE) {
        every { createBrand(any(), any(), any(), any()) } returns null

        assertThatThrownBy { action(NEW_BRAND, USER_ID) }
            .isOfAnyClassIn(IllegalStateException::class.java)
            .hasMessage("Could not save the brand $NEW_BRAND")

        verify(exactly = 1) { createBrand(any(), NEW_BRAND.name, NEW_BRAND.description, USER_ID) }
    }

    @Test
    fun `must call update brand routine and return ID if brand ID is null`() = mockkStatic(ROUTINES_REFERENCE) {
        every { updateBrand(any(), any(), any(), any()) } returns BRAND_ID

        val result = action(BRAND, USER_ID)

        assertThat(result).isEqualTo(BRAND_ID)
        verify(exactly = 1) { updateBrand(any(), BRAND.id, BRAND.name, BRAND.description) }
    }

    @Test
    fun `must throw exception when update brand routine invocation result is null`() = mockkStatic(ROUTINES_REFERENCE) {
        every { updateBrand(any(), any(), any(), any()) } returns null

        assertThatThrownBy { action(BRAND, USER_ID) }
            .isOfAnyClassIn(IllegalStateException::class.java)
            .hasMessage("Could not save the brand $BRAND")

        verify(exactly = 1) { updateBrand(any(), BRAND.id, BRAND.name, BRAND.description) }
    }
}
