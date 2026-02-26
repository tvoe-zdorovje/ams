package by.anatolyloyko.ams.administration.studio.action

import by.anatolyloyko.ams.administration.BRAND_ID
import by.anatolyloyko.ams.administration.ROUTINES_REFERENCE
import by.anatolyloyko.ams.administration.STUDIO_ID
import by.anatolyloyko.ams.administration.USER_ID
import by.anatolyloyko.ams.orm.jooq.schemas.administration.routines.references.createStudio
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import org.assertj.core.api.WithAssertions
import org.jooq.DSLContext
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class JooqCreateStudioActionTest : WithAssertions {
    private val dslContext = mockk<DSLContext>(relaxed = true)

    private val action = JooqCreateStudioAction(dslContext)

    @Test
    fun `must call create studio routine`() = mockkStatic(ROUTINES_REFERENCE) {
        val studioId = STUDIO_ID
        val brandId = BRAND_ID
        val userId = USER_ID
        every { createStudio(any(), any(), any(), any()) } returns STUDIO_ID

        val result = action(studioId, brandId, userId)

        assertThat(result).isEqualTo(STUDIO_ID)
        verify(exactly = 1) {
            createStudio(
                configuration = any(),
                iStudioId = studioId,
                iBrandId = brandId,
                iUserId = userId
            )
        }
    }

    @Test
    fun `must throw exception when routine call result is null`() = mockkStatic(ROUTINES_REFERENCE) {
        val studioId = STUDIO_ID
        val brandId = BRAND_ID
        val userId = USER_ID
        every { createStudio(any(), any(), any(), any()) } returns null

        assertThrows<IllegalStateException> { action(studioId, brandId, userId) }

        verify(exactly = 1) {
            createStudio(
                configuration = any(),
                iStudioId = studioId,
                iBrandId = brandId,
                iUserId = userId
            )
        }
    }
}
