package by.anatolyloyko.ams.administration.role.graphql.resolver

import by.anatolyloyko.ams.administration.role.command.SaveRoleCommand
import by.anatolyloyko.ams.administration.role.command.input.SaveRoleInput
import by.anatolyloyko.ams.administration.role.graphql.dto.SaveRoleRequest
import by.anatolyloyko.ams.administration.role.model.Role
import by.anatolyloyko.ams.common.infrastructure.service.command.CommandGateway
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.SchemaMapping
import org.springframework.stereotype.Controller

/**
 * Resolver for role-related mutations in the GraphQL API.
 *
 * This resolver provides the entry point for mutations related to role management.
 * Uses the {@link CommandGateway} to execute commands.
 *
 * @see CommandGateway
 */
@Controller
class RoleMutationsResolver(
    private val commandGateway: CommandGateway
) {
    /**
     * Resolves the saveRole mutation for saving a role.
     *
     * @param request the request containing an information about the role.
     * @return the ID of the role.
     *
     * @see SaveRoleCommand
     */
    @SchemaMapping(typeName = "RoleMutations")
    fun saveRole(
        @Argument request: SaveRoleRequest
    ): Long = commandGateway.handle(
        SaveRoleCommand(
            input = SaveRoleInput(
                role = Role(
                    name = request.name,
                    description = request.description,
                ),
                permissions = request.permissions
            )
        )
    )
}
