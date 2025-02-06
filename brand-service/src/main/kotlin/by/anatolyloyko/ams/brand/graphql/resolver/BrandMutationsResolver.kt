package by.anatolyloyko.ams.brand.graphql.resolver

import by.anatolyloyko.ams.brand.command.CreateBrandCommand
import by.anatolyloyko.ams.brand.graphql.dto.CreateBrandRequest
import by.anatolyloyko.ams.brand.model.Brand
import by.anatolyloyko.ams.infrastructure.service.command.CommandGateway
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
     * Resolves the createBrand mutation for creating a new brand.
     *
     * @param request the request containing the brand's information.
     * @return the ID of the newly created brand.
     *
     * @see CreateBrandCommand
     */
    @SchemaMapping(typeName = "BrandMutations")
    fun createBrand(
        @Argument request: CreateBrandRequest
    ): Long = commandGateway.handle(
        CreateBrandCommand(
            input = Brand(
                name = request.name,
                description = request.description,
            )
        )
    )
}
