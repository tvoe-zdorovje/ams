package by.anatolyloyko.ams.administration.brand.command

import by.anatolyloyko.ams.administration.BRAND_ID
import by.anatolyloyko.ams.administration.STUDIO_ID
import by.anatolyloyko.ams.administration.brand.action.AssignStudiosAction
import by.anatolyloyko.ams.administration.brand.command.input.AssignStudiosInput
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.WithAssertions
import org.junit.jupiter.api.Test

class AssignStudiosCommandHandlerTest : WithAssertions {
    private val action = mockk<AssignStudiosAction>(relaxed = true)

    private val handler = AssignStudiosCommandHandler(action)

    private val command = AssignStudiosCommand(
        input = AssignStudiosInput(
            brandId = BRAND_ID,
            studios = listOf(STUDIO_ID)
        )
    )

    @Test
    fun `must invoke the action`() {
        handler.handle(command)

        verify(exactly = 1) {
            action(
                brandId = command.input.brandId,
                studios = command.input.studios
            )
        }
    }
}
