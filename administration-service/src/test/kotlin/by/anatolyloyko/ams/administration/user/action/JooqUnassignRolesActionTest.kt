package by.anatolyloyko.ams.administration.user.action

import by.anatolyloyko.ams.administration.ROUTINES_REFERENCE
import by.anatolyloyko.ams.administration.USER_ID
import by.anatolyloyko.ams.orm.jooq.schemas.routines.references.deleteUserRoles
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import org.assertj.core.api.WithAssertions
import org.jooq.DSLContext
import org.junit.jupiter.api.Test

class JooqUnassignRolesActionTest : WithAssertions {
    private val dslContext = mockk<DSLContext>(relaxed = true)

    private val action = JooqUnassignRolesAction(dslContext)

    @Test
    fun `must call delete user roles routine`() = mockkStatic(ROUTINES_REFERENCE) {
        val userId = USER_ID
        val roles = listOf(1L, 2L, 3L)
        every { deleteUserRoles(any(), any(), any()) } returns null

        action(userId, roles)

        verify(exactly = 1) {
            deleteUserRoles(
                configuration = any(),
                iUserId = userId,
                iRoles = roles.toTypedArray()
            )
        }
    }
}
