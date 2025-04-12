package by.anatolyloyko.ams.administration.brand.action

import by.anatolyloyko.ams.administration.BRAND_ID
import by.anatolyloyko.ams.administration.NEW_ROLE
import by.anatolyloyko.ams.administration.PERMISSION
import by.anatolyloyko.ams.administration.ROLE_ID
import by.anatolyloyko.ams.administration.ROUTINES_REFERENCE
import by.anatolyloyko.ams.orm.jooq.schemas.routines.references.createBrandRole
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import org.assertj.core.api.WithAssertions
import org.jooq.DSLContext
import org.junit.jupiter.api.Test

class JooqCreateBrandRoleActionTest : WithAssertions {
    private val dslContext = mockk<DSLContext>(relaxed = true)

    private val action = JooqCreateBrandRoleAction(dslContext)

    @Test
    fun `must call create brand role routine and return generated role ID`() = mockkStatic(ROUTINES_REFERENCE) {
        val brandId = BRAND_ID
        val roleId = ROLE_ID
        val role = NEW_ROLE
        val permissions = listOf(PERMISSION.id)
        every { createBrandRole(any(), any(), any(), any(), any()) } returns roleId

        val result = action(brandId, role, permissions)

        assertThat(result).isEqualTo(roleId)
        verify(exactly = 1) {
            createBrandRole(
                configuration = any(),
                iBrandId = brandId,
                iName = role.name,
                iDescription = role.description,
                iPermissions = permissions.toTypedArray()
            )
        }
    }

    @Test
    fun `must throw an exception when routine invocation result is null`() = mockkStatic(ROUTINES_REFERENCE) {
        val brandId = BRAND_ID
        val role = NEW_ROLE
        val permissions = listOf(PERMISSION.id)
        every { createBrandRole(any(), any(), any(), any(), any()) } returns null

        assertThatThrownBy { action(brandId, role, permissions) }
            .isOfAnyClassIn(IllegalStateException::class.java)
            .hasMessage("Could not create a new role $role with permissions $permissions for brand $brandId")

        verify(exactly = 1) {
            createBrandRole(
                configuration = any(),
                iBrandId = brandId,
                iName = role.name,
                iDescription = role.description,
                iPermissions = permissions.toTypedArray()
            )
        }
    }
}
