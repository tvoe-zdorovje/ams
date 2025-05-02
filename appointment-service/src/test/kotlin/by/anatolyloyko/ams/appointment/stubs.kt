package by.anatolyloyko.ams.appointment

import by.anatolyloyko.ams.appointment.model.Appointment
import by.anatolyloyko.ams.appointment.model.AppointmentStatus
import java.util.UUID

const val CLIENT_USER_ID = 100000001413121100

const val MASTER_USER_ID = 100000002413121100

const val MANAGER_USER_ID = 100000003413121100

const val STUDIO_ID = 1000000000171342100

val APPOINTMENT_ID: UUID = UUID.fromString("d33eff62-6e91-4bb3-a8aa-af6643587566")

val NEW_APPOINTMENT = Appointment(
    description = "Appointment description",
    clientUserId = CLIENT_USER_ID,
    masterUserId = MASTER_USER_ID,
    managerUserId = MANAGER_USER_ID,
    studioId = STUDIO_ID,
    status = AppointmentStatus.REQUESTED,
    comment = "Best regards"
)

val APPOINTMENT = NEW_APPOINTMENT.copy(id = APPOINTMENT_ID)
