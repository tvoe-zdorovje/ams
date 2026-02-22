package by.anatolyloyko.ams.brand.command

import by.anatolyloyko.ams.brand.BRAND
import by.anatolyloyko.ams.brand.BRAND_ID
import by.anatolyloyko.ams.brand.NEW_BRAND
import by.anatolyloyko.ams.brand.USER_ID
import by.anatolyloyko.ams.brand.action.CreateBrandAction
import by.anatolyloyko.ams.brand.action.UpdateBrandAction
import by.anatolyloyko.ams.brand.kafka.KafkaProducer
import by.anatolyloyko.ams.brand.model.Brand
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.WithAssertions
import org.junit.jupiter.api.Test

class SaveBrandCommandHandlerTest : WithAssertions {
    private val dbCreateBrandAction = mockk<CreateBrandAction> {
        every { this@mockk(any<Brand>()) } returns BRAND_ID
    }
    private val kafkaProducer = mockk<KafkaProducer> {
        every { sendBrandCreated(any<Brand>(), any()) } returns BRAND_ID
    }
    private val updateBrandAction = mockk<UpdateBrandAction> {
        every { this@mockk(any<Brand>()) } returns BRAND_ID
    }

    private val handler = SaveBrandCommandHandler(dbCreateBrandAction, updateBrandAction, kafkaProducer)

    @Test
    fun `must invoke create actions if ID is null`() {
        val command = SaveBrandCommand(
            input = NEW_BRAND,
            loggedUserId = USER_ID,
        )

        val result = handler.handle(command)

        assertThat(result).isEqualTo(BRAND_ID)
        verify(exactly = 1) { dbCreateBrandAction(command.input) }
        verify(exactly = 1) {
            kafkaProducer.sendBrandCreated(
                eq(command.input.copy(id = BRAND_ID)),
                eq(USER_ID)
            )
        }
    }

    @Test
    fun `must invoke update action if ID is not null`() {
        val command = SaveBrandCommand(
            input = BRAND,
            loggedUserId = USER_ID,
        )

        val result = handler.handle(command)

        assertThat(result).isEqualTo(BRAND_ID)
        verify(exactly = 1) { updateBrandAction(command.input) }
    }
}
