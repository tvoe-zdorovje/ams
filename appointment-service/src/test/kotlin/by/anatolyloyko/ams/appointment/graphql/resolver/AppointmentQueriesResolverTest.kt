package by.anatolyloyko.ams.appointment.graphql.resolver

import by.anatolyloyko.ams.appointment.APPOINTMENT
import by.anatolyloyko.ams.appointment.query.GetAppointmentQuery
import by.anatolyloyko.ams.common.infrastructure.service.query.QueryGateway
import by.anatolyloyko.ams.common.infrastructure.testing.get
import by.anatolyloyko.ams.common.infrastructure.testing.matches
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.graphql.tester.AutoConfigureHttpGraphQlTester
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.graphql.test.tester.WebGraphQlTester

@SpringBootTest
@AutoConfigureHttpGraphQlTester
class AppointmentQueriesResolverTest {
    @Autowired
    lateinit var graphQlTester: WebGraphQlTester

    @MockkBean
    lateinit var queryGateway: QueryGateway

    @Test
    fun `must return appointment`() {
        every { queryGateway.handle(any<GetAppointmentQuery>()) } returns APPOINTMENT

        val result = graphQlTester
            .documentName("appointment/getAppointment")
            .variable("id", APPOINTMENT.id)
            .execute()

        result.errors().verify()
        val appointmentPath = "appointments.appointment"
        result["$appointmentPath.id"] matches APPOINTMENT.id
        result["$appointmentPath.name"] matches APPOINTMENT.name
        result["$appointmentPath.description"] matches APPOINTMENT.description
    }
}
