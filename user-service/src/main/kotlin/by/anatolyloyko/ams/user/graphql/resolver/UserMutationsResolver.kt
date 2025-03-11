package by.anatolyloyko.ams.user.graphql.resolver

import by.anatolyloyko.ams.common.infrastructure.service.command.CommandGateway
import by.anatolyloyko.ams.user.command.CreateUserCommand
import by.anatolyloyko.ams.user.graphql.dto.CreateUserRequest
import by.anatolyloyko.ams.user.model.User
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
     * Resolves the createUser mutation for creating a new user.
     *
     * @param request the request containing the user's information.
     * @return the ID of the newly created user.
     *
     * @see CreateUserCommand
     */
    @SchemaMapping(typeName = "UserMutations")
    fun createUser(
        @Argument request: CreateUserRequest
    ): Long = commandGateway.handle(
        CreateUserCommand(
            input = User(
                firstName = request.firstName,
                lastName = request.lastName
            )
        )
    )
}
