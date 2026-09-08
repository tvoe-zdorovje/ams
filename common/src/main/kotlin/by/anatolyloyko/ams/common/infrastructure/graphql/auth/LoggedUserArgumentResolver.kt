package by.anatolyloyko.ams.common.infrastructure.graphql.auth

import by.anatolyloyko.ams.common.infrastructure.graphql.auth.model.LoggedUser
import graphql.schema.DataFetchingEnvironment
import org.springframework.core.MethodParameter
import org.springframework.graphql.data.method.HandlerMethodArgumentResolver
import org.springframework.security.authentication.InternalAuthenticationServiceException
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component

/**
 * Argument resolver that resolves arguments of [LoggedUser] type.
 *
 * Actually, it just gets Spring Security [Authentication] from the GraphQL context and maps to [LoggedUser].
 *
 * @see AuthContextGraphQlInterceptor
 */
@Component
class LoggedUserArgumentResolver : HandlerMethodArgumentResolver {
    /**
     * Determines whether the resolver supports a given method parameter by its type.
     *
     * @param parameter the method parameter to check for support.
     * @return `true` if the parameter is annotated with [Principal] and its type is [LoggedUser],
     * `false` otherwise.
     */
    override fun supportsParameter(parameter: MethodParameter): Boolean =
        parameter.hasParameterAnnotation(Principal::class.java)
            && parameter.parameterType.kotlin == LoggedUser::class

    /**
     * Resolves the argument value for a method parameter from the GraphQL context.
     *
     * @param parameter the method parameter to resolve.
     * @param environment the current [DataFetchingEnvironment] from which the context is accessed.
     * @return the resolved value.
     * @throws InternalAuthenticationServiceException if [Authentication] is not found in the GraphQL context
     *                                                  or authentication name is null or is not numeric
     */
    override fun resolveArgument(
        parameter: MethodParameter,
        environment: DataFetchingEnvironment
    ): LoggedUser = environment
        .graphQlContext
        .get<Authentication>(CONTEXT_AUTHENTICATION)
        ?.let { it.name?.toLongOrNull() }
        ?.let { LoggedUser(it) }
        ?: throw InternalAuthenticationServiceException("Authentication name (sub) is missing or invalid.")
}
