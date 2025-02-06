package by.anatolyloyko.ams.user.query

import by.anatolyloyko.ams.user.USER
import by.anatolyloyko.ams.user.USER_ID
import by.anatolyloyko.ams.user.finder.UserFinder
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.WithAssertions
import org.junit.jupiter.api.Test

class GetUserQueryHandlerTest : WithAssertions {
    private val userFinder = mockk<UserFinder> {
        every { findById(USER_ID) } returns USER
    }
    private val handler = GetUserQueryHandler(userFinder)

    private val query = GetUserQuery(input = USER_ID)

    @Test
    fun `must invoke the action`() {
        val result = handler.handle(query)

        assertThat(result).isEqualTo(USER)
        verify(exactly = 1) { userFinder.findById(query.input)  }
    }
}
