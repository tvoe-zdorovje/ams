package by.anatolyloyko.ams.appointment.action

import by.anatolyloyko.ams.appointment.model.Appointment
import by.anatolyloyko.ams.orm.jooq.schemas.routines.references.createAppointment
import org.jooq.DSLContext
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Component
@Transactional
class JooqCreateAppointmentAction(
    private val dslContext: DSLContext
) : CreateAppointmentAction {
    override fun invoke(appointment: Appointment): UUID = createAppointment(
        dslContext.configuration(),
        appointment.description,
        appointment.clientUserId,
        appointment.masterUserId,
        appointment.managerUserId,
        appointment.studioId,
        appointment.comment,
    )
        ?: error("Could not create the following appointment $appointment")
}
