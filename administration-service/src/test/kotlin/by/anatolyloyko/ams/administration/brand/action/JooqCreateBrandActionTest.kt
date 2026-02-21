package by.anatolyloyko.ams.administration.brand.action

import by.anatolyloyko.ams.administration.BRAND_ID
import by.anatolyloyko.ams.administration.ROUTINES_REFERENCE
import by.anatolyloyko.ams.administration.USER_ID
import by.anatolyloyko.ams.orm.jooq.schemas.administration.routines.references.createBrand
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import org.assertj.core.api.WithAssertions
import org.jooq.DSLContext
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class JooqCreateBrandActionTest : WithAssertions {
    private val dslContext = mockk<DSLContext>(relaxed = true)

    private val action = JooqCreateBrandAction(dslContext)

    @Test
    fun `must call create brand routine`() = mockkStatic(ROUTINES_REFERENCE) {
        val brandId = BRAND_ID
        val userId = USER_ID
        every { createBrand(any(), any(), any()) } returns BRAND_ID

        val result = action(brandId, userId)

        assertThat(result).isEqualTo(BRAND_ID)
        verify(exactly = 1) {
            createBrand(
                configuration = any(),
                iBrandId = brandId,
                iUserId = userId
            )
        }
    }

    @Test
    fun `must throw exception when routine call result is null`() = mockkStatic(ROUTINES_REFERENCE) {
        val brandId = BRAND_ID
        val userId = USER_ID
        every { createBrand(any(), any(), any()) } returns null

        assertThrows<IllegalStateException> { action(brandId, userId) }

        verify(exactly = 1) {
            createBrand(
                configuration = any(),
                iBrandId = brandId,
                iUserId = userId
            )
        }
    }
}