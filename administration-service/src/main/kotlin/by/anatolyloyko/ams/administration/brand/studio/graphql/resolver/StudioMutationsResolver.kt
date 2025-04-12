package by.anatolyloyko.ams.administration.brand.studio.graphql.resolver

import by.anatolyloyko.ams.administration.brand.studio.command.CreateStudioRoleCommand
import by.anatolyloyko.ams.administration.brand.studio.command.input.CreateStudioRoleInput
import by.anatolyloyko.ams.administration.brand.studio.graphql.dto.CreateStudioRoleRequest
import by.anatolyloyko.ams.administration.role.model.Role
import by.anatolyloyko.ams.common.infrastructure.service.command.CommandGateway
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
     * Resolves the createRole mutation for creating a new role for a studio.
     *
     * @param request the request containing an information about the studio role and its permissions.
     * @return the ID of the created role.
     *
     * @see CreateStudioRoleCommand
     */
    @SchemaMapping(typeName = "StudioAdministrationMutations")
    fun createRole(
        @Argument request: CreateStudioRoleRequest
    ): Long = commandGateway.handle(
        CreateStudioRoleCommand(
            input = CreateStudioRoleInput(
                studioId = request.studioId,
                role = Role(
                    name = request.name,
                    description = request.description,
                ),
                permissions = request.permissions
            )
        )
    )
}
