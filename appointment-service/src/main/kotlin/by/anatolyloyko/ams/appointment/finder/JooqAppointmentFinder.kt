package by.anatolyloyko.ams.appointment.finder

import by.anatolyloyko.ams.appointment.model.Appointment
import by.anatolyloyko.ams.appointment.model.AppointmentStatus
import by.anatolyloyko.ams.orm.jooq.schemas.tables.records.AppointmentRecord
import by.anatolyloyko.ams.orm.jooq.schemas.tables.references.APPOINTMENT
import by.anatolyloyko.ams.orm.jooq.util.eq
import org.jooq.DSLContext
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

private val MAPPER: (AppointmentRecord) -> Appointment = {
    Appointment(
        id = it.id,
        description = it.description,
        clientUserId = it.clientUserId,
        masterUserId = it.masterUserId,
        managerUserId = it.managerUserId,
        studioId = it.studioId,
        status = AppointmentStatus.valueOf(it.status!!.literal),
        comment = it.comment ?: ""
    )
}

@Component
@Transactional(readOnly = true)
class JooqAppointmentFinder(
    private val dslContext: DSLContext
) : AppointmentFinder {
    override fun findById(id: UUID): Appointment? = dslContext
        .selectFrom(APPOINTMENT)
        .where(APPOINTMENT.ID eq id)
        .fetchOne(MAPPER)
}
