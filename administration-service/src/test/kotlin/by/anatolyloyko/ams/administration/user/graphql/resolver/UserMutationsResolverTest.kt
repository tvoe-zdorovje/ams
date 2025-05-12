package by.anatolyloyko.ams.administration.user.graphql.resolver

import by.anatolyloyko.ams.administration.ROLE_ID
import by.anatolyloyko.ams.administration.USER_ID
import by.anatolyloyko.ams.administration.user.command.AssignRolesCommand
import by.anatolyloyko.ams.administration.user.command.UnassignRolesCommand
import by.anatolyloyko.ams.administration.user.command.input.UserRolesInput
import by.anatolyloyko.ams.common.infrastructure.service.command.CommandGateway
import by.anatolyloyko.ams.common.infrastructure.testing.get
import by.anatolyloyko.ams.common.infrastructure.testing.matches
import com.ninjasquad.springmockk.MockkBean
import io.mockk.verify
import org.junit.jupiter.api.Nested
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

    @MockkBean(relaxed = true)
    lateinit var commandGateway: CommandGateway

    @Nested
    inner class AssignRoles {
        @Test
        fun `must assign role to user`() {
            val userId = USER_ID
            val roles = listOf(ROLE_ID)

            val result = graphQlTester
                .documentName("user/assignRoles")
                .variable("userId", userId)
                .variable("roles", roles)
                .execute()

            result.errors().verify()
            result["users.assignRoles"] matches true

            verify(exactly = 1) {
                commandGateway.handle(
                    match<AssignRolesCommand> {
                        it.input == UserRolesInput(
                            userId = userId,
                            roles = roles,
                        )
                    }
                )
            }
        }

        @Test
        fun `must return error if list of roles is empty`() {
            val result = graphQlTester
                .documentName("user/assignRoles")
                .variable("userId", USER_ID)
                .variable("roles", emptyList<Long>())
                .execute()

            result.errors().expect {
                it.message == "Parameter 'roles' must not be empty!"
            }

            verify(exactly = 0) { commandGateway.handle(any()) }
        }
    }

    @Nested
    inner class UnassignRoles {
        @Test
        fun `must unassign role from user`() {
            val userId = USER_ID
            val roles = listOf(ROLE_ID)

            val result = graphQlTester
                .documentName("user/unassignRoles")
                .variable("userId", userId)
                .variable("roles", roles)
                .execute()

            result.errors().verify()
            result["users.unassignRoles"] matches true

            verify(exactly = 1) {
                commandGateway.handle(
                    match<UnassignRolesCommand> {
                        it.input == UserRolesInput(
                            userId = userId,
                            roles = roles
                        )
                    }
                )
            }
        }

        @Test
        fun `must return error if list of roles is empty`() {
            val result = graphQlTester
                .documentName("user/unassignRoles")
                .variable("userId", USER_ID)
                .variable("roles", emptyList<Long>())
                .execute()

            result.errors().expect {
                it.message == "Parameter 'roles' must not be empty!"
            }

            verify(exactly = 0) { commandGateway.handle(any()) }
        }
    }
}
