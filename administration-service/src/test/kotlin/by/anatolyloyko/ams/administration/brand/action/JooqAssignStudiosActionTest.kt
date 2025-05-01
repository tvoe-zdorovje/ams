package by.anatolyloyko.ams.administration.brand.action

import by.anatolyloyko.ams.administration.BRAND_ID
import by.anatolyloyko.ams.administration.ROUTINES_REFERENCE
import by.anatolyloyko.ams.administration.STUDIO_ID
import by.anatolyloyko.ams.orm.jooq.schemas.routines.references.addBrandStudios
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import org.assertj.core.api.WithAssertions
import org.jooq.DSLContext
import org.junit.jupiter.api.Test

class JooqAssignStudiosActionTest : WithAssertions {
    private val dslContext = mockk<DSLContext>(relaxed = true)

    private val action = JooqAssignStudiosAction(dslContext)

    @Test
    fun `must call add brand studios routine`() = mockkStatic(ROUTINES_REFERENCE) {
        val brandId = BRAND_ID
        val studios = listOf(STUDIO_ID)
        every { addBrandStudios(any(), any(), any()) } returns null

        action(brandId, studios)

        verify(exactly = 1) {
            addBrandStudios(
                configuration = any(),
                iBrandId = brandId,
                iStudios = studios.toTypedArray()
            )
        }
    }
}
