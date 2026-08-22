package by.anatolyloyko.ams.user.graphql.resolver

import by.anatolyloyko.ams.common.infrastructure.service.command.CommandGateway
import by.anatolyloyko.ams.commontest.graphql.GraphQLTest
import by.anatolyloyko.ams.commontest.graphql.get
import by.anatolyloyko.ams.commontest.graphql.loginAs
import by.anatolyloyko.ams.commontest.graphql.matches
import by.anatolyloyko.ams.user.EXTERNAL_USER_ID
import by.anatolyloyko.ams.user.USER_ID
import by.anatolyloyko.ams.user.command.CreateUserCommand
import by.anatolyloyko.ams.user.command.UpdateUserCommand
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.graphql.test.tester.WebGraphQlTester

@GraphQLTest
class UserMutationsResolverTest {
    @Autowired
    lateinit var graphQlTester: WebGraphQlTester

    @MockkBean
    lateinit var commandGateway: CommandGateway

    @Test
    @Disabled
    fun `must create user`() {
        every { commandGateway.handle(any<CreateUserCommand>()) } returns USER_ID

        val result = graphQlTester
            .documentName("user/createUser")
            .variable("externalId", EXTERNAL_USER_ID)
            .variable("firstName", "Alexey")
            .variable("lastName", "Kasimov")
            .variable("phoneNumber", "+375297671245")
            .execute()

        result.errors().verify()
        result["users.createUser"] matches USER_ID
    }

    @Test
    fun `must update user`() {
        every { commandGateway.handle(any<UpdateUserCommand>()) } returns USER_ID

        val result = graphQlTester
            .loginAs(USER_ID)
            .documentName("user/updateUser")
            .variable("firstName", "Alexey")
            .variable("lastName", "Kasimov")
            .variable("phoneNumber", "+375297671245")
            .execute()

        result.errors().verify()
        result["users.updateUser"] matches USER_ID
    }
}
