package by.anatolyloyko.ams.appointment.graphql.resolver

import by.anatolyloyko.ams.appointment.query.GetAppointmentQuery
import by.anatolyloyko.ams.common.infrastructure.service.query.QueryGateway
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.SchemaMapping
import org.springframework.stereotype.Controller
import java.util.UUID

/**
 * Resolver for appointment-related queries in the GraphQL API.
 *
 * This resolver provides the entry point for queries related to appointment management.
 * Uses the {@link QueryGateway} to execute queries.
 *
 * @see QueryGateway
 */
@Controller
class AppointmentQueriesResolver(
    private val queryGateway: QueryGateway
) {
    /**
     * Resolver for finding an appointment by ID.
     *
     * @param id the identifier of the appointment to retrieve.
     * @return the appointment data, or `null` if the appointment is not found.
     *
     * @see GetAppointmentQuery
     */
    @SchemaMapping(typeName = "AppointmentQueries")
    fun appointment(
        @Argument id: UUID
    ) = queryGateway.handle(GetAppointmentQuery(id))
}
