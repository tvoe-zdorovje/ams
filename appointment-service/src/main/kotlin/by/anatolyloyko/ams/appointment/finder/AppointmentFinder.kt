package by.anatolyloyko.ams.appointment.finder

import by.anatolyloyko.ams.appointment.model.Appointment

/**
 * Finder responsible for finding a appointment.
 */
interface AppointmentFinder {
    /**
     * Finds a appointment by ID.
     *
     * @param id the ID of the appointment.
     * @return the found {@link Appointment}, or `null` if no appointment is found.
     */
    fun findById(id: Long): Appointment?
}
