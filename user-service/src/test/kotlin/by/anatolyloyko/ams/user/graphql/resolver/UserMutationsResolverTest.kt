package by.anatolyloyko.ams.user.graphql.resolver

import by.anatolyloyko.ams.common.infrastructure.service.command.CommandGateway
import by.anatolyloyko.ams.common.infrastructure.testing.get
import by.anatolyloyko.ams.common.infrastructure.testing.matches
import by.anatolyloyko.ams.user.USER_ID
import by.anatolyloyko.ams.user.USER_PASSWORD
import by.anatolyloyko.ams.user.command.CreateUserCommand
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.graphql.tester.AutoConfigureHttpGraphQlTester
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.graphql.test.tester.WebGraphQlTester

@SpringBootTest
@AutoConfigureHttpGraphQlTester
class UserMutationsResolverTest {
    @Autowired
    lateinit var graphQlTester: WebGraphQlTester

    @MockkBean
    lateinit var commandGateway: CommandGateway

    @Test
    fun `must create user`() {
        every { commandGateway.handle(any<CreateUserCommand>()) } returns USER_ID

        val result = graphQlTester
            .documentName("user/createUser")
            .variable("password", USER_PASSWORD)
            .variable("firstName", "Alexey")
            .variable("lastName", "Kasimov")
            .variable("phoneNumber", "+375297671245")
            .execute()

        result.errors().verify()
        result["users.createUser"] matches USER_ID
    }
}
