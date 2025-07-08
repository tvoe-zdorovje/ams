package by.anatolyloyko.ams.studio.graphql.resolver

import by.anatolyloyko.ams.common.infrastructure.service.command.CommandGateway
import by.anatolyloyko.ams.studio.command.SaveStudioCommand
import by.anatolyloyko.ams.studio.graphql.dto.CreateStudioRequest
import by.anatolyloyko.ams.studio.graphql.dto.UpdateStudioRequest
import by.anatolyloyko.ams.studio.model.Studio
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.SchemaMapping
import org.springframework.stereotype.Controller

/**
 * Resolver for studio-related mutations in the GraphQL API.
 *
 * This resolver provides the entry point for mutations related to studio management.
 * Uses the {@link CommandGateway} to execute commands.
 *
 * @see CommandGateway
 */
@Controller
class StudioMutationsResolver(
    private val commandGateway: CommandGateway
) {
    /**
     * Resolves the createStudio mutation for creating a new studio.
     *
     * @param request the request containing the studio's information.
     * @return the ID of the newly created studio.
     *
     * @see SaveStudioCommand
     */
    @SchemaMapping(typeName = "StudioMutations")
    fun createStudio(
        @Argument request: CreateStudioRequest
    ): Long = commandGateway.handle(
        SaveStudioCommand(
            input = Studio(
                name = request.name,
                description = request.description,
            )
        )
    )

    /**
     * Resolves the updateStudio mutation for updating a studio.
     *
     * @param request the request containing an updated studio's information.
     * @return the ID of the updated studio.
     *
     * @see SaveStudioCommand
     */
    @Suppress("ForbiddenComment")
    // TODO: auth & permissions control
    @SchemaMapping(typeName = "StudioMutations")
    fun updateStudio(
        @Argument request: UpdateStudioRequest
    ): Long = commandGateway.handle(
        SaveStudioCommand(
            input = Studio(
                id = request.organizationId,
                name = request.name,
                description = request.description,
            )
        )
    )
}
