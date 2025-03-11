package by.anatolyloyko.ams.appointment.graphql.resolver

import by.anatolyloyko.ams.appointment.APPOINTMENT
import by.anatolyloyko.ams.appointment.APPOINTMENT_ID
import by.anatolyloyko.ams.appointment.command.CreateAppointmentCommand
import by.anatolyloyko.ams.common.infrastructure.service.command.CommandGateway
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
class AppointmentMutationsResolverTest {
    @Autowired
    lateinit var graphQlTester: WebGraphQlTester

    @MockkBean
    lateinit var commandGateway: CommandGateway

    @Test
    fun `must create appointment`() {
        every { commandGateway.handle(any<CreateAppointmentCommand>()) } returns APPOINTMENT_ID

        val result = graphQlTester
            .documentName("appointment/createAppointment")
            .variable("name", APPOINTMENT.name)
            .variable("description", APPOINTMENT.description)
            .execute()

        result.errors().verify()
        result["appointments.createAppointment"] matches APPOINTMENT_ID
    }
}
