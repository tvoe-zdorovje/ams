package by.anatolyloyko.ams.appointment.query

import by.anatolyloyko.ams.appointment.model.Appointment
import by.anatolyloyko.ams.common.infrastructure.service.query.BaseQuery
import java.util.UUID

/**
 * A query to retrieve a appointment {@link Appointment} by ID {@link Long}.
 *
 * @param input the ID of the appointment to retrieve.
 */
class GetAppointmentQuery(
    input: UUID
) : BaseQuery<UUID, Appointment?>(input)
