package by.anatolyloyko.ams.appointment.command

import by.anatolyloyko.ams.appointment.action.CreateAppointmentAction
import by.anatolyloyko.ams.common.infrastructure.service.command.BaseCommandHandler
import org.springframework.stereotype.Component

/**
 * Handles {@link CreateAppointmentCommand}.
 *
 * Creates a new appointment based on the provided data.
 */
@Component
class CreateAppointmentCommandHandler(
    private val createAppointmentAction: CreateAppointmentAction
) : BaseCommandHandler<CreateAppointmentCommand, Long>() {
    override fun handleInternal(command: CreateAppointmentCommand): Long = createAppointmentAction(command.input)
}
