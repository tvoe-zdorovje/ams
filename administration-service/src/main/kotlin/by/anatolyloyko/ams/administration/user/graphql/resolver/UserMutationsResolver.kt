package by.anatolyloyko.ams.administration.user.graphql.resolver

import by.anatolyloyko.ams.administration.user.command.AssignRolesCommand
import by.anatolyloyko.ams.administration.user.command.UnassignRolesCommand
import by.anatolyloyko.ams.administration.user.command.input.UserRolesInput
import by.anatolyloyko.ams.common.infrastructure.service.command.CommandGateway
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.SchemaMapping
import org.springframework.stereotype.Controller

/**
 * Resolver for user-related mutations in the GraphQL API.
 *
 * This resolver provides the entry point for mutations related to user management.
 * Uses the {@link CommandGateway} to execute commands.
 *
 * @see CommandGateway
 */
@Controller
class UserMutationsResolver(
    private val commandGateway: CommandGateway
) {
    /**
     * Resolves the assignRoles mutation for assigning roles to a user.
     *
     * @param userId the target user ID.
     * @param roles IDs of roles.
     * @return 'true' as the successful result.
     *
     * @see AssignRolesCommand
     */
    @SchemaMapping(typeName = "UserMutations")
    fun assignRoles(
        @Argument userId: Long,
        @Argument roles: List<Long>
    ): Boolean = commandGateway.handle(
        AssignRolesCommand(
            input = UserRolesInput(
                userId = userId,
                roles = roles
                    .ifEmpty { error("Parameter 'roles' must not be empty!") }
            )
        )
    )
        .let { true }

    /**
     * Resolves the unassignRoles mutation for unassigning roles from a user.
     *
     * @param userId the target user ID.
     * @param roles IDs of roles.
     * @return 'true' as the successful result.
     *
     * @see UnassignRolesCommand
     */
    @SchemaMapping(typeName = "UserMutations")
    fun unassignRoles(
        @Argument userId: Long,
        @Argument roles: List<Long>
    ): Boolean = commandGateway.handle(
        UnassignRolesCommand(
            input = UserRolesInput(
                userId = userId,
                roles = roles
                    .ifEmpty { error("Parameter 'roles' must not be empty!") }
            )
        )
    )
        .let { true }
}
