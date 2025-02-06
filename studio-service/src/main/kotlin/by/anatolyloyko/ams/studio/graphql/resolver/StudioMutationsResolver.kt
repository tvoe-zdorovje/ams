package by.anatolyloyko.ams.studio.graphql.resolver

import by.anatolyloyko.ams.infrastructure.service.command.CommandGateway
import by.anatolyloyko.ams.studio.command.CreateStudioCommand
import by.anatolyloyko.ams.studio.graphql.dto.CreateStudioRequest
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
     * @see CreateStudioCommand
     */
    @SchemaMapping(typeName = "StudioMutations")
    fun createStudio(
        @Argument request: CreateStudioRequest
    ): Long = commandGateway.handle(
        CreateStudioCommand(
            input = Studio(
                name = request.name,
                description = request.description,
            )
        )
    )
}
