package by.anatolyloyko.ams.appointment.database

import by.anatolyloyko.ams.appointment.model.Appointment
import org.springframework.stereotype.Repository
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicLong

/**
 * Temporary dummy implementation. Mocks DB
 */
@Repository
class DummyAppointmentRepository {
    private val idGenerator = AtomicLong(0)
    private val appointmentTable = ConcurrentHashMap<Long, Appointment>()

    fun save(appointment: Appointment): Long {
        val appointmentWithId = if (appointment.id == null) {
            appointment.copy(id = idGenerator.incrementAndGet())
        } else {
            appointment
        }

        appointmentTable[appointmentWithId.id!!] = appointmentWithId

        return appointmentWithId.id
    }

    fun findById(id: Long): Appointment? = appointmentTable[id]
}
