package by.anatolyloyko.ams.appointment.command

import by.anatolyloyko.ams.appointment.action.CreateAppointmentAction
import by.anatolyloyko.ams.common.infrastructure.service.command.BaseCommandHandler
import org.springframework.stereotype.Component
import java.util.UUID

/**
 * Handles {@link CreateAppointmentCommand}.
 *
 * Creates a new appointment based on the provided data.
 */
@Component
class CreateAppointmentCommandHandler(
    private val createAppointmentAction: CreateAppointmentAction
) : BaseCommandHandler<CreateAppointmentCommand, UUID>() {
    override fun handleInternal(command: CreateAppointmentCommand): UUID = createAppointmentAction(command.input)
}
