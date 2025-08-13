package by.anatolyloyko.ams.administration.user.query

import by.anatolyloyko.ams.administration.BRAND_ID
import by.anatolyloyko.ams.administration.ROLE
import by.anatolyloyko.ams.administration.USER_ID
import by.anatolyloyko.ams.administration.user.finder.UserRolesFinder
import by.anatolyloyko.ams.administration.user.query.input.GetUserRolesQueryInput
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.WithAssertions
import org.junit.jupiter.api.Test

class GetUserRolesQueryHandlerTest : WithAssertions {
    private val query = GetUserRolesQuery(
        input = GetUserRolesQueryInput(
            userId = USER_ID,
            organizationId = BRAND_ID
        )
    )

    private val userRoles = listOf(ROLE)

    private val finder = mockk<UserRolesFinder> {
        every {
            findByOrganizationId(
                userId = query.input.userId,
                organizationId = query.input.organizationId
            )
        } returns userRoles
    }

    private val handler = GetUserRolesQueryHandler(finder)

    @Test
    fun `must call finder and return result`() {
        assertThat(handler.handle(query)).isEqualTo(userRoles)
    }
}
