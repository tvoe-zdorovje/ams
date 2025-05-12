package by.anatolyloyko.ams.administration.brand.graphql.resolver

import by.anatolyloyko.ams.administration.brand.command.AssignStudiosCommand
import by.anatolyloyko.ams.administration.brand.command.CreateBrandRoleCommand
import by.anatolyloyko.ams.administration.brand.command.input.AssignStudiosInput
import by.anatolyloyko.ams.administration.brand.command.input.CreateBrandRoleInput
import by.anatolyloyko.ams.administration.brand.graphql.dto.CreateBrandRoleRequest
import by.anatolyloyko.ams.administration.role.model.Role
import by.anatolyloyko.ams.common.infrastructure.service.command.CommandGateway
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.SchemaMapping
import org.springframework.stereotype.Controller

/**
 * Resolver for brand-related mutations in the GraphQL API.
 *
 * This resolver provides the entry point for mutations related to brand management.
 * Uses the {@link CommandGateway} to execute commands.
 *
 * @see CommandGateway
 */
@Controller
class BrandMutationsResolver(
    private val commandGateway: CommandGateway
) {
    /**
     * Resolves the createRole mutation for creating a new role for a brand.
     *
     * @param request the request containing an information about the brand role and its permissions.
     * @return the ID of the created role.
     *
     * @see CreateBrandRoleCommand
     */
    @SchemaMapping(typeName = "BrandMutations")
    fun createRole(
        @Argument request: CreateBrandRoleRequest
    ): Long = commandGateway.handle(
        CreateBrandRoleCommand(
            input = CreateBrandRoleInput(
                brandId = request.brandId,
                role = Role(
                    name = request.name,
                    description = request.description,
                ),
                permissions = request.permissions
            )
        )
    )

    /**
     * Resolves the assignStudios mutation for assigning provided studios to a brand.
     *
     * @param brandId the target brand ID.
     * @param studios the list of studio IDs.
     * @return 'true' as a successful result
     *
     * @see AssignStudiosCommand
     */
    @SchemaMapping(typeName = "BrandMutations")
    fun assignStudios(
        @Argument brandId: Long,
        @Argument studios: List<Long>,
    ): Boolean = commandGateway.handle(
        AssignStudiosCommand(
            input = AssignStudiosInput(
                brandId = brandId,
                studios = studios
                    .ifEmpty { error("Parameter 'studios' must not be empty!") }
            )
        )
    )
        .let { true }
}
