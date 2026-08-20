package by.anatolyloyko.ams.common.infrastructure.graphql.directive

import by.anatolyloyko.ams.common.infrastructure.exception.AccessDeniedException
import by.anatolyloyko.ams.common.infrastructure.graphql.auth.CONTEXT_AUTHENTICATION
import graphql.language.ArrayValue
import graphql.language.StringValue
import graphql.schema.DataFetchingEnvironment
import graphql.schema.GraphQLFieldDefinition
import graphql.schema.idl.SchemaDirectiveWiring
import graphql.schema.idl.SchemaDirectiveWiringEnvironment
import org.springframework.security.authentication.InternalAuthenticationServiceException
import org.springframework.security.authorization.AuthorizationDeniedException
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken

private const val DIRECTIVE_NAME = "requiresPermissions"

private const val DIRECTIVE_ARGUMENT_NAME = "permissions"

private const val INPUT_ARGUMENT_NAME_ORGANIZATION_ID = "organizationId"

private val INPUT_NAMES = setOf("request", "input", "payload")

private const val JWT_PERMISSIONS_CLAIM = "permissions"

/**
 * Handles the GraphQL `@requiresPermissions` directive.
 *
 * This directive is used to enforce permission-based access control at the GraphQL schema level.
 * When applied to a field or query, it ensures that an authentication contains all required authorities
 * before the resolver logic is executed.
 *
 * The directive performs the following steps:
 * 1. Extracts required permissions from the GraphQL schema.
 * 2. Retrieves [org.springframework.security.core.Authentication] from the GraphQL context.
 * 3. Determines authentication's authorities within the scope of the provided `organizationId` argument.
 * 4. Validates that the user has all the required permissions specified by the directive.
 *
 * Works with [org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken] implementation.
 * 
 * If the user lacks any of the required permissions, the [AccessDeniedException] is thrown.
 */
class RequiresPermissionsDirective : SchemaDirectiveWiring {
    override fun onField(
        environment: SchemaDirectiveWiringEnvironment<GraphQLFieldDefinition>
    ): GraphQLFieldDefinition {
        val originalDataFetcher = environment.fieldDataFetcher
        val requiredPermissions = getRequiredPermissions(environment)

        return environment.setFieldDataFetcher { dataFetchingEnv ->
            val organizationId = dataFetchingEnv.getOrganizationId()


            val permissions = dataFetchingEnv.getAuthenticationPermissionsFor(organizationId)
            if (!permissions.containsAll(requiredPermissions)) {
                throw AuthorizationDeniedException("insufficient permissions - ${requiredPermissions - permissions}.")
            }

            originalDataFetcher[dataFetchingEnv]
        }
    }

    private fun getRequiredPermissions(
        environment: SchemaDirectiveWiringEnvironment<GraphQLFieldDefinition>
    ): List<String> {
        val argument = environment
            .element
            .getAppliedDirective(DIRECTIVE_NAME)
            .getArgument(DIRECTIVE_ARGUMENT_NAME)
            .argumentValue.value as ArrayValue
        return argument.values.map { (it as StringValue).value }
    }

    private fun DataFetchingEnvironment.getOrganizationId(): Long = getInput()
        ?.get(INPUT_ARGUMENT_NAME_ORGANIZATION_ID)
        ?.toString()
        ?.toLongOrNull()
        ?: throw AuthorizationDeniedException("'$INPUT_ARGUMENT_NAME_ORGANIZATION_ID' argument is missed or invalid.")

    private fun DataFetchingEnvironment.getInput() = INPUT_NAMES.firstNotNullOfOrNull {
        getArgument<Map<String, Any?>>(it)
    }

    private fun DataFetchingEnvironment.getAuthenticationPermissionsFor(organizationId: Long): Set<String> {
        val authentication = graphQlContext.get<JwtAuthenticationToken>(CONTEXT_AUTHENTICATION)
            ?: throw InternalAuthenticationServiceException("GraphQL context doesn't contain a valid authentication.")

        return authentication
            .token
            .getClaim<Map<String, Set<String>>>(JWT_PERMISSIONS_CLAIM)[organizationId.toString()]
            ?: emptySet()
    }
}
