package by.anatolyloyko.ams.appointment.finder

import by.anatolyloyko.ams.appointment.model.Appointment
import java.util.UUID

/**
 * Finder responsible for finding a appointment.
 */
interface AppointmentFinder {
    /**
     * Finds an appointment by UUID.
     *
     * @param id the UUID of the appointment.
     * @return the found {@link Appointment}, or `null` if no appointment is found.
     */
    fun findById(id: UUID): Appointment?
}
