package by.anatolyloyko.ams.appointment

import by.anatolyloyko.ams.appointment.model.Appointment

const val APPOINTMENT_ID = 1L

val NEW_APPOINTMENT = Appointment(
    name = "Lidskoe",
    description = "We brew the best beer around Lida",
)

val APPOINTMENT = NEW_APPOINTMENT.copy(id = APPOINTMENT_ID)
