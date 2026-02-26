package by.anatolyloyko.ams.studio.action

import by.anatolyloyko.ams.orm.jooq.schemas.studios.routines.references.createStudio
import by.anatolyloyko.ams.studio.NEW_STUDIO
import by.anatolyloyko.ams.studio.ROUTINES_REFERENCE
import by.anatolyloyko.ams.studio.STUDIO_ID
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import org.assertj.core.api.WithAssertions
import org.jooq.DSLContext
import org.junit.jupiter.api.Test

class JooqCreateStudioActionTest : WithAssertions {
    private val dslContext = mockk<DSLContext>(relaxed = true)

    private val action = JooqCreateStudioAction(dslContext)

    @Test
    fun `must call create studio routine and return ID`() = mockkStatic(ROUTINES_REFERENCE) {
        every { createStudio(any(), any(), any()) } returns STUDIO_ID

        val result = action(NEW_STUDIO)

        assertThat(result).isEqualTo(STUDIO_ID)
        verify(exactly = 1) {
            createStudio(
                configuration = any(),
                iName = NEW_STUDIO.name,
                iDescription = NEW_STUDIO.description
            )
        }
    }

    @Test
    fun `must throw exception when create studio routine invocation result = null`() = mockkStatic(ROUTINES_REFERENCE) {
        every { createStudio(any(), any(), any()) } returns null

        assertThatThrownBy { action(NEW_STUDIO) }
            .isOfAnyClassIn(IllegalStateException::class.java)
            .hasMessage("Could not save the studio $NEW_STUDIO")

        verify(exactly = 1) {
            createStudio(
                configuration = any(),
                iName = NEW_STUDIO.name,
                iDescription = NEW_STUDIO.description
            )
        }
    }
}
