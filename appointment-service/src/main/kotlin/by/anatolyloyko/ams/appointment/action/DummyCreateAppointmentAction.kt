package by.anatolyloyko.ams.appointment.action

import by.anatolyloyko.ams.appointment.database.DummyAppointmentRepository
import by.anatolyloyko.ams.appointment.model.Appointment
import org.springframework.stereotype.Component

/**
 * Temporary dummy implementation.
 */
@Component
class DummyCreateAppointmentAction(
    private val dummyAppointmentRepository: DummyAppointmentRepository
) : CreateAppointmentAction {
    override fun invoke(appointment: Appointment): Long = dummyAppointmentRepository.save(appointment)
}
