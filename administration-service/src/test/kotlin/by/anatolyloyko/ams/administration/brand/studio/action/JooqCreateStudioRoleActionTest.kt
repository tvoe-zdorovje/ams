package by.anatolyloyko.ams.administration.brand.studio.action

import by.anatolyloyko.ams.administration.NEW_ROLE
import by.anatolyloyko.ams.administration.PERMISSION
import by.anatolyloyko.ams.administration.ROLE_ID
import by.anatolyloyko.ams.administration.ROUTINES_REFERENCE
import by.anatolyloyko.ams.administration.STUDIO_ID
import by.anatolyloyko.ams.orm.jooq.schemas.routines.references.createStudioRole
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import org.assertj.core.api.WithAssertions
import org.jooq.DSLContext
import org.junit.jupiter.api.Test

class JooqCreateStudioRoleActionTest : WithAssertions {
    private val dslContext = mockk<DSLContext>(relaxed = true)

    private val action = JooqCreateStudioRoleAction(dslContext)

    @Test
    fun `must call create studio role routine and return generated role ID`() = mockkStatic(ROUTINES_REFERENCE) {
        val studioId = STUDIO_ID
        val roleId = ROLE_ID
        val role = NEW_ROLE
        val permissions = listOf(PERMISSION.id)
        every { createStudioRole(any(), any(), any(), any(), any()) } returns roleId

        val result = action(studioId, role, permissions)

        assertThat(result).isEqualTo(roleId)
        verify(exactly = 1) {
            createStudioRole(
                configuration = any(),
                iStudioId = studioId,
                iName = role.name,
                iDescription = role.description,
                iPermissions = permissions.toTypedArray()
            )
        }
    }

    @Test
    fun `must throw an exception when routine invocation result is null`() = mockkStatic(ROUTINES_REFERENCE) {
        val studioId = STUDIO_ID
        val role = NEW_ROLE
        val permissions = listOf(PERMISSION.id)
        every { createStudioRole(any(), any(), any(), any(), any()) } returns null

        assertThatThrownBy { action(studioId, role, permissions) }
            .isOfAnyClassIn(IllegalStateException::class.java)
            .hasMessage("Could not create a new role $role with permissions $permissions for studio $studioId")

        verify(exactly = 1) {
            createStudioRole(
                configuration = any(),
                iStudioId = studioId,
                iName = role.name,
                iDescription = role.description,
                iPermissions = permissions.toTypedArray()
            )
        }
    }
}
