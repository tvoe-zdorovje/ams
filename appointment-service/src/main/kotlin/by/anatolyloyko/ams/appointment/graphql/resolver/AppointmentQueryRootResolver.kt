package by.anatolyloyko.ams.appointment.graphql.resolver

import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller

/**
 * Root resolver for appointment-related queries.
 *
 * This resolver serves as the entry point for appointment queries in the GraphQL API.
 * It delegates the query handling to the {@link AppointmentQueriesResolver},
 * so actually it just provides an additional level of abstraction for logically separating domain-related queries.
 *
 * @see AppointmentQueriesResolver
 */
@Controller
class AppointmentQueryRootResolver(
    private val appointmentQueriesResolver: AppointmentQueriesResolver
) {
    @QueryMapping
    fun appointments() = appointmentQueriesResolver
}
