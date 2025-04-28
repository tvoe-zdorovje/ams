package by.anatolyloyko.ams.administration.role.action

import by.anatolyloyko.ams.administration.NEW_ROLE
import by.anatolyloyko.ams.administration.ROLE_ID
import by.anatolyloyko.ams.administration.ROUTINES_REFERENCE
import by.anatolyloyko.ams.orm.jooq.schemas.routines.references.saveRole
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import org.assertj.core.api.WithAssertions
import org.jooq.DSLContext
import org.junit.jupiter.api.Test

class JooqSaveRoleActionTest : WithAssertions {
    private val dslContext = mockk<DSLContext>(relaxed = true)

    private val action = JooqSaveRoleAction(dslContext)

    @Test
    fun `must call save role without permissions routine and return ID`() = mockkStatic(ROUTINES_REFERENCE) {
        every { saveRole(any(), any(), any(), any(), any()) } returns ROLE_ID

        val result = action(NEW_ROLE)

        assertThat(result).isEqualTo(ROLE_ID)
        verify(exactly = 1) { saveRole(any(), NEW_ROLE.id, NEW_ROLE.name, NEW_ROLE.description, emptyArray()) }
    }

    @Test
    fun `must call save role with permissions routine and return ID`() = mockkStatic(ROUTINES_REFERENCE) {
        val permissions = listOf(1L, 2L, 3L)

        every { saveRole(any(), any(), any(), any(), any()) } returns ROLE_ID

        val result = action(NEW_ROLE, permissions)

        assertThat(result).isEqualTo(ROLE_ID)
        verify(exactly = 1) {
            saveRole(any(), NEW_ROLE.id, NEW_ROLE.name, NEW_ROLE.description, permissions.toTypedArray())
        }
    }

    @Test
    fun `must throw an exception when routine invocation result is null`() = mockkStatic(ROUTINES_REFERENCE) {
        every { saveRole(any(), any(), any(), any(), any()) } returns null

        assertThatThrownBy { action(NEW_ROLE) }
            .isOfAnyClassIn(IllegalStateException::class.java)
            .hasMessage("Could not save role $NEW_ROLE")

        verify(exactly = 1) { saveRole(any(), NEW_ROLE.id, NEW_ROLE.name, NEW_ROLE.description, emptyArray()) }
    }
}
