package by.anatolyloyko.ams.studio.action

import by.anatolyloyko.ams.orm.jooq.schemas.routines.references.saveStudio
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

class JooqSaveStudioActionTest : WithAssertions {
    private val dslContext = mockk<DSLContext>(relaxed = true)

    private val action = JooqSaveStudioAction(dslContext)

    @Test
    fun `must call save studio routine and return ID`() = mockkStatic(ROUTINES_REFERENCE) {
        every { saveStudio(any(), any(), any(), any()) } returns STUDIO_ID

        val result = action(NEW_STUDIO)

        assertThat(result).isEqualTo(STUDIO_ID)
        verify(exactly = 1) { saveStudio(any(), NEW_STUDIO.id, NEW_STUDIO.name, NEW_STUDIO.description) }
    }

    @Test
    fun `must throw an exception when routine invocation result is null`() = mockkStatic(ROUTINES_REFERENCE) {
        every { saveStudio(any(), any(), any(), any()) } returns null

        assertThatThrownBy { action(NEW_STUDIO) }
            .isOfAnyClassIn(IllegalStateException::class.java)
            .hasMessage("Could not save the studio $NEW_STUDIO")

        verify(exactly = 1) { saveStudio(any(), NEW_STUDIO.id, NEW_STUDIO.name, NEW_STUDIO.description) }
    }
}
