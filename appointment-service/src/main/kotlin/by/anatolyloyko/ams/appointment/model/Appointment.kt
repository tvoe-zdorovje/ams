package by.anatolyloyko.ams.appointment.model

import java.util.UUID

data class Appointment(
    val id: UUID? = null,
    val description: String,
    val clientUserId: Long,
    val masterUserId: Long,
    val managerUserId: Long,
    val studioId: Long,
    val status: AppointmentStatus? = null,
    val comment: String = "",
)
