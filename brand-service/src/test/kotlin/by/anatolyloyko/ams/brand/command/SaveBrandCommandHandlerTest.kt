package by.anatolyloyko.ams.brand.command

import by.anatolyloyko.ams.brand.BRAND_ID
import by.anatolyloyko.ams.brand.NEW_BRAND
import by.anatolyloyko.ams.brand.action.SaveBrandAction
import by.anatolyloyko.ams.brand.model.Brand
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.WithAssertions
import org.junit.jupiter.api.Test

class SaveBrandCommandHandlerTest : WithAssertions {
    private val saveBrandAction = mockk<SaveBrandAction> {
        every { this@mockk(any<Brand>()) } returns BRAND_ID
    }
    private val handler = SaveBrandCommandHandler(saveBrandAction)

    private val command = SaveBrandCommand(
        input = NEW_BRAND,
    )

    @Test
    fun `must invoke the action`() {
        val result = handler.handle(command)

        assertThat(result).isEqualTo(BRAND_ID)
        verify(exactly = 1) { saveBrandAction(command.input) }
    }
}
