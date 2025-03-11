package by.anatolyloyko.ams.appointment.query

import by.anatolyloyko.ams.appointment.finder.AppointmentFinder
import by.anatolyloyko.ams.appointment.model.Appointment
import by.anatolyloyko.ams.common.infrastructure.service.query.BaseQueryHandler
import org.springframework.stereotype.Component

/**
 * Handles {@link GetAppointmentQuery}.
 *
 * Find a appointment based on the provided data.
 */
@Component
class GetAppointmentQueryHandler(
    private val appointmentFinder: AppointmentFinder,
) : BaseQueryHandler<GetAppointmentQuery, Appointment?>() {
    override fun handleInternal(query: GetAppointmentQuery): Appointment? = appointmentFinder.findById(query.input)
}
