package by.anatolyloyko.ams.appointment.query

import by.anatolyloyko.ams.appointment.APPOINTMENT
import by.anatolyloyko.ams.appointment.APPOINTMENT_ID
import by.anatolyloyko.ams.appointment.finder.AppointmentFinder
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.WithAssertions
import org.junit.jupiter.api.Test

class GetAppointmentQueryHandlerTest : WithAssertions {
    private val appointmentFinder = mockk<AppointmentFinder> {
        every { findById(APPOINTMENT_ID) } returns APPOINTMENT
    }
    private val handler = GetAppointmentQueryHandler(appointmentFinder)

    private val query = GetAppointmentQuery(input = APPOINTMENT_ID)

    @Test
    fun `must invoke the action`() {
        val result = handler.handle(query)

        assertThat(result).isEqualTo(APPOINTMENT)
        verify(exactly = 1) { appointmentFinder.findById(query.input) }
    }
}
