package by.anatolyloyko.ams.administration.role.query

import by.anatolyloyko.ams.administration.ROLE
import by.anatolyloyko.ams.administration.role.finder.RoleFinder
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.WithAssertions
import org.junit.jupiter.api.Test

class GetRoleQueryHandlerTest : WithAssertions {
    private val roleFinder = mockk<RoleFinder> {
        every { findById(any()) } returns ROLE
    }
    private val handler = GetRoleQueryHandler(roleFinder)

    private val query = GetRoleQuery(
        input = ROLE.id!!,
    )

    @Test
    fun `must invoke the action`() {
        val result = handler.handle(query)

        assertThat(result).isEqualTo(ROLE)
        verify(exactly = 1) {
            roleFinder.findById(ROLE.id!!)
        }
    }
}
