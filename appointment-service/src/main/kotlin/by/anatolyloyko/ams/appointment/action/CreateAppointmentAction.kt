package by.anatolyloyko.ams.appointment.action

import by.anatolyloyko.ams.appointment.model.Appointment
import java.util.UUID

/**
 * Action responsible for creating a new appointment.
 */
interface CreateAppointmentAction {
    /**
     * Creates a new appointment and returns the generated UUID.
     *
     * @param appointment the appointment data.
     * @return the UUID of the newly created appointment.
     */
    operator fun invoke(appointment: Appointment): UUID
}
