package by.anatolyloyko.ams.common.infrastructure.graphql.directive

import by.anatolyloyko.ams.common.infrastructure.exception.AccessDeniedException
import by.anatolyloyko.ams.common.infrastructure.graphql.auth.CONTEXT_LOGGED_USER
import by.anatolyloyko.ams.common.infrastructure.graphql.auth.model.LoggedUserTokenData
import graphql.language.ArrayValue
import graphql.language.StringValue
import graphql.schema.DataFetchingEnvironment
import graphql.schema.GraphQLFieldDefinition
import graphql.schema.idl.SchemaDirectiveWiring
import graphql.schema.idl.SchemaDirectiveWiringEnvironment

private const val DIRECTIVE_NAME = "requiresPermissions"

private const val DIRECTIVE_ARGUMENT_NAME = "permissions"

private const val INPUT_ARGUMENT_NAME_ORGANIZATION_ID = "organizationId"

/**
 * Handles the GraphQL `@requiresPermissions` directive.
 *
 * This directive is used to enforce permission-based access control at the GraphQL schema level.
 * When applied to a field or query, it ensures that the authenticated user has all required permissions
 * before the resolver logic is executed.
 *
 * The directive performs the following steps:
 * 1. Extracts required permissions from the GraphQL schema.
 * 2. Retrieves the authenticated user data from the GraphQL context.
 * 3. Determines the user's existing permissions within the scope of the provided `organizationId` argument.
 * 4. Validates that the user has all the permissions specified by the directive.
 *
 * If the user lacks any of the required permissions, the AccessDeniedException is thrown,
 * and the operation is not executed.
 *
 * @see [LoggedUserWithTokenData]
 * @see [AuthContextGraphQlInterceptor]
 */
class RequiresPermissionsDirective : SchemaDirectiveWiring {
    override fun onField(
        environment: SchemaDirectiveWiringEnvironment<GraphQLFieldDefinition>
    ): GraphQLFieldDefinition {
        val originalDataFetcher = environment.fieldDataFetcher
        val argument = environment
            .element
            .getAppliedDirective(DIRECTIVE_NAME)
            .getArgument(DIRECTIVE_ARGUMENT_NAME)
            .argumentValue.value as ArrayValue
        val requiredPermissions = argument.values.map { (it as StringValue).value }

        return environment.setFieldDataFetcher { dataFetchingEnv ->
            val organizationId = dataFetchingEnv
                .getInput()
                ?.get(INPUT_ARGUMENT_NAME_ORGANIZATION_ID)
                ?.toString()
                ?.toLongOrNull()
                ?: throw AccessDeniedException("'organizationId' argument is missed or invalid.")

            val permissions = dataFetchingEnv
                .graphQlContext
                .get<LoggedUserTokenData>(CONTEXT_LOGGED_USER)
                ?.permissions
                ?.get(organizationId)
                ?: emptySet()

            if (!permissions.containsAll(requiredPermissions)) {
                throw AccessDeniedException("insufficient permissions - ${requiredPermissions - permissions}.")
            }

            originalDataFetcher[dataFetchingEnv]
        }
    }

    private fun DataFetchingEnvironment.getInput() =
        getArgument<Map<String, Any?>>("request")
            ?: getArgument<Map<String, Any?>>("input")
            ?: getArgument<Map<String, Any?>>("payload")
}
