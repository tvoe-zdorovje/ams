package by.anatolyloyko.ams.appointment.query

import by.anatolyloyko.ams.appointment.model.Appointment
import by.anatolyloyko.ams.infrastructure.service.query.BaseQuery

/**
 * A query to retrieve a appointment {@link Appointment} by ID {@link Long}.
 *
 * @param input the ID of the appointment to retrieve.
 */
class GetAppointmentQuery(
    input: Long
) : BaseQuery<Long, Appointment?>(input)
