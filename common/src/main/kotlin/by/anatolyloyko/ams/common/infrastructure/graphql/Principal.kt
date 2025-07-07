package by.anatolyloyko.ams.common.infrastructure.graphql

/**
 * Marks a method parameter that should be resolved as the currently authenticated user.
 *
 * This annotation is typically used in GraphQL resolvers where the authenticated user is injected automatically
 * by a custom argument resolver such as [LoggedUserArgumentResolver].
 */
@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class Principal
