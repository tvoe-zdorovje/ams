package by.anatolyloyko.ams.brand.command

import by.anatolyloyko.ams.brand.BRAND_ID
import by.anatolyloyko.ams.brand.NEW_BRAND
import by.anatolyloyko.ams.brand.action.CreateBrandAction
import by.anatolyloyko.ams.brand.model.Brand
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.WithAssertions
import org.junit.jupiter.api.Test

class CreateBrandCommandHandlerTest : WithAssertions {
    private val createBrandAction = mockk<CreateBrandAction> {
        every { this@mockk(any<Brand>()) } returns BRAND_ID
    }
    private val handler = CreateBrandCommandHandler(createBrandAction)

    private val command = CreateBrandCommand(
        input = NEW_BRAND,
    )

    @Test
    fun `must invoke the action`() {
        val result = handler.handle(command)

        assertThat(result).isEqualTo(BRAND_ID)
        verify(exactly = 1) { createBrandAction(command.input) }
    }
}
