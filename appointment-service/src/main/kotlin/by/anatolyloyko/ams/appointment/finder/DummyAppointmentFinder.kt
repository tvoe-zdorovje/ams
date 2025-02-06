package by.anatolyloyko.ams.appointment.finder

import by.anatolyloyko.ams.appointment.database.DummyAppointmentRepository
import by.anatolyloyko.ams.appointment.model.Appointment
import org.springframework.stereotype.Component

/**
 * Temporary dummy implementation
 */
@Component
class DummyAppointmentFinder(
    private val dummyAppointmentRepository: DummyAppointmentRepository
) : AppointmentFinder {
    override fun findById(id: Long): Appointment? = dummyAppointmentRepository.findById(id)
}
