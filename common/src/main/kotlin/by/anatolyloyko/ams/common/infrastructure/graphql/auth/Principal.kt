package by.anatolyloyko.ams.common.infrastructure.graphql.auth

/**
 * Marks a method parameter that should be resolved by [LoggedUserArgumentResolver].
 *
 * For usage in GraphQL query mappers in order to resolve arguments containing info about currently logged user.
 * This is a kind of alternative to Spring's [@AuthenticationPrincipal]
 * that allows to forget about Spring Security Context and focus on GraphQL context.
 *
 * @see [LoggedUserArgumentResolver]
 */
@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class Principal
