package by.anatolyloyko.ams.appointment.command

import by.anatolyloyko.ams.appointment.APPOINTMENT_ID
import by.anatolyloyko.ams.appointment.NEW_APPOINTMENT
import by.anatolyloyko.ams.appointment.action.CreateAppointmentAction
import by.anatolyloyko.ams.appointment.model.Appointment
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.WithAssertions
import org.junit.jupiter.api.Test

class CreateAppointmentCommandHandlerTest : WithAssertions {
    private val createAppointmentAction = mockk<CreateAppointmentAction> {
        every { this@mockk(any<Appointment>()) } returns APPOINTMENT_ID
    }
    private val handler = CreateAppointmentCommandHandler(createAppointmentAction)

    private val command = CreateAppointmentCommand(
        input = NEW_APPOINTMENT,
    )

    @Test
    fun `must invoke the action`() {
        val result = handler.handle(command)

        assertThat(result).isEqualTo(APPOINTMENT_ID)
        verify(exactly = 1) { createAppointmentAction(command.input) }
    }
}
