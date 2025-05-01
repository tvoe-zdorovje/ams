package by.anatolyloyko.ams.administration.brand.command

import by.anatolyloyko.ams.administration.BRAND_ID
import by.anatolyloyko.ams.administration.NEW_ROLE
import by.anatolyloyko.ams.administration.PERMISSION
import by.anatolyloyko.ams.administration.ROLE_ID
import by.anatolyloyko.ams.administration.brand.action.CreateBrandRoleAction
import by.anatolyloyko.ams.administration.brand.command.input.CreateBrandRoleInput
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.WithAssertions
import org.junit.jupiter.api.Test

class CreateBrandRoleCommandHandlerTest : WithAssertions {
    private val action = mockk<CreateBrandRoleAction> {
        every { this@mockk(any(), any(), any()) } returns ROLE_ID
    }

    private val handler = CreateBrandRoleCommandHandler(action)

    private val command = CreateBrandRoleCommand(
        input = CreateBrandRoleInput(
            brandId = BRAND_ID,
            role = NEW_ROLE,
            permissions = listOf(PERMISSION.id),
        ),
    )

    @Test
    fun `must invoke the action`() {
        val result = handler.handle(command)

        assertThat(result).isEqualTo(ROLE_ID)
        verify(exactly = 1) {
            action(
                brandId = command.input.brandId,
                role = command.input.role,
                permissions = command.input.permissions
            )
        }
    }
}
