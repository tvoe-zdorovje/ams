package by.anatolyloyko.ams.appointment.graphql.resolver

import by.anatolyloyko.ams.appointment.command.CreateAppointmentCommand
import by.anatolyloyko.ams.appointment.graphql.dto.CreateAppointmentRequest
import by.anatolyloyko.ams.appointment.model.Appointment
import by.anatolyloyko.ams.common.infrastructure.service.command.CommandGateway
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.SchemaMapping
import org.springframework.stereotype.Controller

/**
 * Resolver for appointment-related mutations in the GraphQL API.
 *
 * This resolver provides the entry point for mutations related to appointment management.
 * Uses the {@link CommandGateway} to execute commands.
 *
 * @see CommandGateway
 */
@Controller
class AppointmentMutationsResolver(
    private val commandGateway: CommandGateway
) {
    /**
     * Resolves the createAppointment mutation for creating a new appointment.
     *
     * @param request the request containing the appointment's information.
     * @return the ID of the newly created appointment.
     *
     * @see CreateAppointmentCommand
     */
    @SchemaMapping(typeName = "AppointmentMutations")
    fun createAppointment(
        @Argument request: CreateAppointmentRequest
    ): Long = commandGateway.handle(
        CreateAppointmentCommand(
            input = Appointment(
                name = request.name,
                description = request.description,
            )
        )
    )
}
