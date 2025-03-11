package by.anatolyloyko.ams.appointment.command

import by.anatolyloyko.ams.appointment.model.Appointment
import by.anatolyloyko.ams.common.infrastructure.service.command.BaseCommand

/**
 * A command intended for creating a new appointment and returning the generated ID.
 *
 * @param input the appointment data for creation.
 */
class CreateAppointmentCommand(
    input: Appointment
) : BaseCommand<Appointment, Long>(input)
