package by.anatolyloyko.ams.appointment.graphql.resolver

import by.anatolyloyko.ams.appointment.APPOINTMENT
import by.anatolyloyko.ams.appointment.APPOINTMENT_ID
import by.anatolyloyko.ams.appointment.CLIENT_USER_ID
import by.anatolyloyko.ams.appointment.command.CreateAppointmentCommand
import by.anatolyloyko.ams.common.infrastructure.service.command.CommandGateway
import by.anatolyloyko.ams.commontest.graphql.GraphQLTest
import by.anatolyloyko.ams.commontest.graphql.get
import by.anatolyloyko.ams.commontest.graphql.loginAs
import by.anatolyloyko.ams.commontest.graphql.matches
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.graphql.test.tester.WebGraphQlTester

@GraphQLTest
class AppointmentMutationsResolverTest {
    @Autowired
    lateinit var graphQlTester: WebGraphQlTester

    @MockkBean
    lateinit var commandGateway: CommandGateway

    @Test
    fun `must create appointment`() {
        every { commandGateway.handle(any<CreateAppointmentCommand>()) } returns APPOINTMENT_ID

        val result = graphQlTester
            .loginAs(CLIENT_USER_ID)
            .documentName("appointment/createAppointment")
            .variable("description", APPOINTMENT.description)
            .variable("clientUserId", APPOINTMENT.clientUserId)
            .variable("masterUserId", APPOINTMENT.masterUserId)
            .variable("managerUserId", APPOINTMENT.managerUserId)
            .variable("studioId", APPOINTMENT.studioId)
            .variable("comment", APPOINTMENT.comment)
            .execute()

        result.errors().verify()
        result["appointments.createAppointment"] matches APPOINTMENT_ID
    }
}
