package by.anatolyloyko.ams.appointment.graphql.resolver

import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.stereotype.Controller

/**
 * Root resolver for appointment-related mutations.
 *
 * This resolver serves as the entry point for appointment mutations in the GraphQL API.
 * It delegates the mutation handling to the {@link AppointmentMutationsResolver},
 * so actually it just provides an additional level of abstraction for logically separating domain-related mutations.
 *
 * This resolver serves as the entry point for appointment mutations in the GraphQL API.
 *
 * @see AppointmentMutationsResolver
 */

@Controller
class AppointmentMutationRootResolver(
    private val appointmentMutationsResolver: AppointmentMutationsResolver
) {
    @MutationMapping
    fun appointments() = appointmentMutationsResolver
}
