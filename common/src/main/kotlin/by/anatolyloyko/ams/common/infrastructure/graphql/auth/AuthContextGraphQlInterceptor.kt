package by.anatolyloyko.ams.common.infrastructure.graphql.auth

import org.springframework.graphql.server.WebGraphQlInterceptor
import org.springframework.graphql.server.WebGraphQlRequest
import org.springframework.graphql.server.WebGraphQlResponse
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty

internal const val CONTEXT_AUTHENTICATION = "authentication"

/**
 * GraphQL interceptor that puts an Authentification from the Spring Security Context into the GraphQL context.
 */
@Component
class AuthContextGraphQlInterceptor : WebGraphQlInterceptor {
    override fun intercept(
        request: WebGraphQlRequest,
        chain: WebGraphQlInterceptor.Chain
    ): Mono<WebGraphQlResponse> = Mono.justOrEmpty(SecurityContextHolder.getContext().authentication)
        .flatMap { authentication ->
            request.configureExecutionInput { _, builder ->
                builder
                    .graphQLContext {
                        it.put(CONTEXT_AUTHENTICATION, authentication)
                    }
                    .build()
            }

            chain.next(request)
        }
        .switchIfEmpty { chain.next(request) }
}
