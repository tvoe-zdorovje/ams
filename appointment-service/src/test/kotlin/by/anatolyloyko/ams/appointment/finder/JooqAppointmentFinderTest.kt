package by.anatolyloyko.ams.appointment.finder

import by.anatolyloyko.ams.appointment.APPOINTMENT
import by.anatolyloyko.ams.appointment.APPOINTMENT_ID
import by.anatolyloyko.ams.appointment.JooqTest
import by.anatolyloyko.ams.orm.jooq.schemas.enums.AppointmentStatus
import by.anatolyloyko.ams.orm.jooq.schemas.tables.records.AppointmentRecord
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.util.UUID

class JooqAppointmentFinderTest : JooqTest() {
    @Autowired
    private lateinit var finder: JooqAppointmentFinder

    @BeforeAll
    fun beforeAll() {
        dslContext.executeInsert(
            AppointmentRecord(
                id = APPOINTMENT.id!!,
                description = APPOINTMENT.description,
                clientUserId = APPOINTMENT.clientUserId,
                masterUserId = APPOINTMENT.masterUserId,
                managerUserId = APPOINTMENT.managerUserId,
                studioId = APPOINTMENT.studioId,
                status = AppointmentStatus.valueOf(APPOINTMENT.status!!.name),
                comment = APPOINTMENT.comment,
            )
        )
    }

    @Test
    fun `must return null if no appointments found by id`() {
        val result = finder.findById(UUID.randomUUID())

        assertThat(result).isNull()
    }

    @Test
    fun `must find and return appointment by id`() {
        val result = finder.findById(APPOINTMENT_ID)

        assertThat(result).isEqualTo(APPOINTMENT)
    }
}
