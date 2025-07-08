package by.anatolyloyko.ams.brand.graphql.resolver

import by.anatolyloyko.ams.brand.command.SaveBrandCommand
import by.anatolyloyko.ams.brand.graphql.dto.CreateBrandRequest
import by.anatolyloyko.ams.brand.graphql.dto.UpdateBrandRequest
import by.anatolyloyko.ams.brand.model.Brand
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
     * Resolves the createBrand mutation for creating a new brand.
     *
     * @param request the request containing the brand's information.
     * @return the ID of the newly created brand.
     *
     * @see SaveBrandCommand
     */
    @SchemaMapping(typeName = "BrandMutations")
    fun createBrand(
        @Argument request: CreateBrandRequest
    ): Long = commandGateway.handle(
        SaveBrandCommand(
            input = Brand(
                name = request.name,
                description = request.description,
            )
        )
    )

    /**
     * Resolves the updateBrand mutation for updating a brand.
     *
     * @param request the request containing the brand's information.
     * @return the ID of the updated brand.
     *
     * @see SaveBrandCommand
     */
    @Suppress("ForbiddenComment")
    // TODO: auth & permissions control
    @SchemaMapping(typeName = "BrandMutations")
    fun updateBrand(
        @Argument request: UpdateBrandRequest
    ): Long = commandGateway.handle(
        SaveBrandCommand(
            input = Brand(
                id = request.organizationId,
                name = request.name,
                description = request.description,
            )
        )
    )
}
