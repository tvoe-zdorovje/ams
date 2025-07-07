package by.anatolyloyko.ams.common.infrastructure.graphql

import by.anatolyloyko.ams.common.infrastructure.exception.AuthenticationException
import graphql.schema.DataFetchingEnvironment
import org.springframework.core.MethodParameter
import org.springframework.graphql.data.method.HandlerMethodArgumentResolver
import org.springframework.stereotype.Component

/**
 * Argument resolver for extracting the currently authenticated user from the GraphQL context.
 *
 * This resolver checks for the presence of the [Principal] annotation on a method parameter.
 * If present, it attempts to resolve the argument by retrieving the user from the GraphQL context.
 *
 * If no user is found, an [AuthenticationException] is thrown, indicating that authentication is required.
 *
 * @see LoggedUserGraphQlInterceptor
 * @see Principal
 */
@Component
class LoggedUserArgumentResolver : HandlerMethodArgumentResolver {
    /**
     * Determines whether the resolver supports a given method parameter.
     *
     * @param parameter the method parameter to check for support.
     * @return `true` if the parameter is annotated with [Principal], `false` otherwise.
     */
    override fun supportsParameter(parameter: MethodParameter): Boolean =
        parameter.hasParameterAnnotation(Principal::class.java)

    /**
     * Resolves the argument value for a method parameter from the GraphQL context.
     *
     * @param parameter the method parameter to resolve.
     * @param environment the current [DataFetchingEnvironment] from which the context is accessed.
     * @return the resolved value.
     * @throws AuthenticationException if no value is found in the GraphQL context.
     */
    override fun resolveArgument(parameter: MethodParameter, environment: DataFetchingEnvironment): Any = environment
        .graphQlContext
        .get<Any>(ARGUMENT_LOGGED_USER)
        ?: throw AuthenticationException("Authentication required")
}
