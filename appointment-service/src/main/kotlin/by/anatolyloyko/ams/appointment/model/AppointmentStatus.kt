package by.anatolyloyko.ams.appointment.model

enum class AppointmentStatus {
    REQUESTED, // -- requested or corrected by a client
    CORRECTED, // -- corrected by authorized staff
    APPROVED, //  -- approved by authorized staff
    COMPLETED, // -- completed
    CANCELLED //  -- cancelled by a client or authorized staff
}
