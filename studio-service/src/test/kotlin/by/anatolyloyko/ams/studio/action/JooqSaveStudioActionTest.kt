package by.anatolyloyko.ams.studio.action

import by.anatolyloyko.ams.orm.jooq.schemas.studios.routines.references.createStudio
import by.anatolyloyko.ams.orm.jooq.schemas.studios.routines.references.updateStudio
import by.anatolyloyko.ams.studio.NEW_STUDIO
import by.anatolyloyko.ams.studio.ROUTINES_REFERENCE
import by.anatolyloyko.ams.studio.STUDIO
import by.anatolyloyko.ams.studio.STUDIO_ID
import by.anatolyloyko.ams.studio.USER_ID
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
    fun `must call create studio routine and return ID if studio ID is not null`() = mockkStatic(ROUTINES_REFERENCE) {
        every { createStudio(any(), any(), any(), any()) } returns STUDIO_ID

        val result = action(NEW_STUDIO, USER_ID)

        assertThat(result).isEqualTo(STUDIO_ID)
        verify(exactly = 1) { createStudio(any(), NEW_STUDIO.name, NEW_STUDIO.description, USER_ID) }
    }


    @Test
    fun `must throw exception when create studio routine invocation result = null`() = mockkStatic(ROUTINES_REFERENCE) {
        every { createStudio(any(), any(), any(), any()) } returns null

        assertThatThrownBy { action(NEW_STUDIO, USER_ID) }
            .isOfAnyClassIn(IllegalStateException::class.java)
            .hasMessage("Could not save the studio $NEW_STUDIO")

        verify(exactly = 1) { createStudio(any(), NEW_STUDIO.name, NEW_STUDIO.description, USER_ID) }
    }

    @Test
    fun `must call update studio routine and return ID if studio ID is null`() = mockkStatic(ROUTINES_REFERENCE) {
        every { updateStudio(any(), any(), any(), any()) } returns STUDIO_ID

        val result = action(STUDIO, USER_ID)

        assertThat(result).isEqualTo(STUDIO_ID)
        verify(exactly = 1) { updateStudio(any(), STUDIO.id, STUDIO.name, STUDIO.description) }
    }

    @Test
    fun `must throw exception when update studio routine invocation result = null`() = mockkStatic(ROUTINES_REFERENCE) {
        every { updateStudio(any(), any(), any(), any()) } returns null

        assertThatThrownBy { action(STUDIO, USER_ID) }
            .isOfAnyClassIn(IllegalStateException::class.java)
            .hasMessage("Could not save the studio $STUDIO")

        verify(exactly = 1) { updateStudio(any(), STUDIO.id, STUDIO.name, STUDIO.description) }
    }
}
