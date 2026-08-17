package by.anatolyloyko.ams.gateway.filter

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.Ordered
import org.springframework.data.redis.core.ReactiveStringRedisTemplate
import org.springframework.http.HttpStatus
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.server.ResponseStatusException
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty
import reactor.kotlin.core.publisher.toMono

/**
 * Web filter that exchanges authentication tokens by looking up tokens in Redis
 * and setting them as Bearer authentication headers for downstream requests.
 *
 * ## Behavior
 * The filter performs the following steps for each incoming request:
 * 1. Extracts the authenticated user's name from [ReactiveSecurityContextHolder]
 * 2. Looks up the user's token in Redis using the authentication name as the key
 * 3. If found, sets the token in the `Authorization` header as `Bearer <token>`
 * 4. Passes the modified request to the next filter in the chain
 *
 * ## Error Cases
 * Returns **HTTP 401 Unauthorized** if:
 * - Security context is empty or unavailable
 * - Authentication name is null or blank
 * - No token is found in Redis for the given authentication name
 *
 * ## Example Redis Key Structure
 * ```
 * Key: "<key-prefix>:username"
 * Value: "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
 * ```
 *
 * @see WebFilter
 * @see Ordered
 * @see ReactiveStringRedisTemplate
 */
@Component
class TokenExchangeWebFilter(
    private val redisTemplate: ReactiveStringRedisTemplate,
    @param:Value("\${ams.redis.topic.user-tokens.key-prefix}")
    private val redisKeyPrefix: String
) : WebFilter {
    private val log = LoggerFactory.getLogger(this::class.java)

    /**
     * Applies the token exchange filter to the HTTP request.
     *
     * @param exchange the current [ServerWebExchange]
     * @param chain the [WebFilterChain] to delegate to after token processing
     * @return a [Mono] that completes when the request has been processed
     * @throws ResponseStatusException with 401 Unauthorized if authentication or token lookup fails
     */
    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> =
        getAuthentificationName()
            .flatMap(::lookupToken)
            .map { exchange.setBearerAuth(it) }
            .flatMap(chain::filter)
            .doOnError { log.error("Error while executing token exchange", it) }

    /**
     * Extracts the authenticated user's name from the current security context.
     *
     * @return a [Mono] containing the authentication name
     * @throws ResponseStatusException with 401 Unauthorized if:
     *         - Security context is not available
     *         - Authentication is null
     *         - Authentication name is null or blank
     */
    private fun getAuthentificationName(): Mono<String> =
        ReactiveSecurityContextHolder
            .getContext()
            .flatMap { context ->
                context
                    .authentication
                    ?.name
                    ?.takeIf(String::isNotEmpty)
                    .toMono()
            }
            .switchIfEmpty { Mono.error(unauthorized("Unauthenticated")) }


    /**
     * Looks up the authentication token from Redis using the provided authentication name.
     *
     * @param authentificationName the user's unique authentication name to use as Redis key
     * @return a [Mono] containing the token value retrieved from Redis
     * @throws ResponseStatusException with 401 Unauthorized if no token is found in Redis
     *         for the given authentication name
     */
    private fun lookupToken(authentificationName: String): Mono<String> =
        redisTemplate
            .opsForValue()
            .get(redisKeyPrefix + authentificationName)
            .switchIfEmpty { Mono.error(unauthorized("No token found for user $authentificationName")) }

    /**
     * Sets the Bearer authentication token in the request's Authorization header.
     *
     * Creates a new [ServerWebExchange] with the provided token added as
     * `Authorization: Bearer <token>` header.
     *
     * @receiver the [ServerWebExchange] to modify
     * @param token the authentication token value (typically a JWT)
     * @return a new [ServerWebExchange] with the token set in the Authorization header
     */
    private fun ServerWebExchange.setBearerAuth(token: String): ServerWebExchange =
        mutate()
            .request { request ->
                request.headers { headers ->
                    headers.setBearerAuth(token)
                }
            }
            .build()

    /**
     * Creates an HTTP 401 Unauthorized exception with the provided message.
     *
     * @param message the error message to include in the response
     * @return a [ResponseStatusException] with 401 status code
     */
    private fun unauthorized(message: String): ResponseStatusException =
        ResponseStatusException(HttpStatus.UNAUTHORIZED, message)
}
