package by.anatolyloyko.ams.studio.query

import by.anatolyloyko.ams.studio.STUDIO
import by.anatolyloyko.ams.studio.STUDIO_ID
import by.anatolyloyko.ams.studio.finder.StudioFinder
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.WithAssertions
import org.junit.jupiter.api.Test

class GetStudioQueryHandlerTest : WithAssertions {
    private val studioFinder = mockk<StudioFinder> {
        every { findById(STUDIO_ID) } returns STUDIO
    }
    private val handler = GetStudioQueryHandler(studioFinder)

    private val query = GetStudioQuery(input = STUDIO_ID)

    @Test
    fun `must invoke the action`() {
        val result = handler.handle(query)

        assertThat(result).isEqualTo(STUDIO)
        verify(exactly = 1) { studioFinder.findById(query.input) }
    }
}
