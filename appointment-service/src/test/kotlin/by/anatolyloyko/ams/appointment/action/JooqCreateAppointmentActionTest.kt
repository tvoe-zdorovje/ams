package by.anatolyloyko.ams.appointment.action

import by.anatolyloyko.ams.appointment.APPOINTMENT_ID
import by.anatolyloyko.ams.appointment.NEW_APPOINTMENT
import by.anatolyloyko.ams.appointment.ROUTINES_REFERENCE
import by.anatolyloyko.ams.orm.jooq.schemas.routines.references.createAppointment
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import org.assertj.core.api.WithAssertions
import org.jooq.DSLContext
import org.junit.jupiter.api.Test

class JooqCreateAppointmentActionTest : WithAssertions {
    private val dslContext = mockk<DSLContext>(relaxed = true)

    private val action = JooqCreateAppointmentAction(dslContext)

    @Test
    fun `must call create new appointment and return ID`() = mockkStatic(ROUTINES_REFERENCE) {
        val appointmentId = APPOINTMENT_ID
        val appointment = NEW_APPOINTMENT
        every { createAppointment(any(), any(), any(), any(), any(), any(), any()) } returns appointmentId

        val result = action(appointment)

        assertThat(result).isEqualTo(appointmentId)
        verify(exactly = 1) {
            createAppointment(
                configuration = any(),
                iDescription = appointment.description,
                iClientUserId = appointment.clientUserId,
                iMasterUserId = appointment.masterUserId,
                iManagerUserId = appointment.managerUserId,
                iStudioId = appointment.studioId,
                iComment = appointment.comment
            )
        }
    }

    @Test
    fun `must throw an exception when routine invocation result is null`() = mockkStatic(ROUTINES_REFERENCE) {
        val appointment = NEW_APPOINTMENT

        every { createAppointment(any(), any(), any(), any(), any(), any(), any()) } returns null

        assertThatThrownBy { action(appointment) }
            .isOfAnyClassIn(IllegalStateException::class.java)
            .hasMessage("Could not create the following appointment $appointment")

        verify(exactly = 1) {
            createAppointment(
                configuration = any(),
                iDescription = appointment.description,
                iClientUserId = appointment.clientUserId,
                iMasterUserId = appointment.masterUserId,
                iManagerUserId = appointment.managerUserId,
                iStudioId = appointment.studioId,
                iComment = appointment.comment
            )
        }
    }
}
