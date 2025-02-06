package by.anatolyloyko.ams.appointment.action

import by.anatolyloyko.ams.appointment.model.Appointment

/**
 * Action responsible for creating a new appointment.
 */
interface CreateAppointmentAction {
    /**
     * Creates a new appointment and returns the generated ID.
     *
     * @param appointment the appointment data.
     * @return the ID of the newly created appointment.
     */
    operator fun invoke(appointment: Appointment): Long
}
