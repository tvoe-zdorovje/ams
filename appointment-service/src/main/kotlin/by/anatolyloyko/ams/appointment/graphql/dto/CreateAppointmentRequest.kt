package by.anatolyloyko.ams.appointment.graphql.dto

data class CreateAppointmentRequest(
    val description: String,
    val clientUserId: Long,
    val masterUserId: Long,
    val managerUserId: Long,
    val studioId: Long,
    val comment: String,
)
