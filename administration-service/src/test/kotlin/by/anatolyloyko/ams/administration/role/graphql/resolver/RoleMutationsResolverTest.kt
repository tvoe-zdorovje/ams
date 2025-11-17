package by.anatolyloyko.ams.administration.role.graphql.resolver

import by.anatolyloyko.ams.administration.ROLE
import by.anatolyloyko.ams.administration.STUDIO_ID
import by.anatolyloyko.ams.administration.USER_ID
import by.anatolyloyko.ams.administration.role.command.SaveRoleCommand
import by.anatolyloyko.ams.administration.role.command.input.SaveRoleInput
import by.anatolyloyko.ams.common.infrastructure.service.command.CommandGateway
import by.anatolyloyko.ams.common.infrastructure.testing.expectForbidden
import by.anatolyloyko.ams.common.infrastructure.testing.expectUnauthorized
import by.anatolyloyko.ams.common.infrastructure.testing.get
import by.anatolyloyko.ams.common.infrastructure.testing.loginAs
import by.anatolyloyko.ams.common.infrastructure.testing.matches
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.graphql.tester.AutoConfigureHttpGraphQlTester
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.graphql.test.tester.WebGraphQlTester

@SpringBootTest
@AutoConfigureHttpGraphQlTester
class RoleMutationsResolverTest {
    @Autowired
    lateinit var graphQlTester: WebGraphQlTester

    @MockkBean
    lateinit var commandGateway: CommandGateway

    @Test
    fun `must save role`() {
        val permissions = listOf(1L, 2L, 3L)
        every { commandGateway.handle(any<SaveRoleCommand>()) } returns ROLE.id!!

        val result = graphQlTester
            .loginAs(USER_ID, STUDIO_ID,"SaveRole")
            .documentName("role/saveRole")
            .variable("organizationId", STUDIO_ID)
            .variable("id", ROLE.id)
            .variable("name", ROLE.name)
            .variable("description", ROLE.description)
            .variable("permissions", permissions)
            .execute()

        result.errors().verify()
        result["roles.saveRole"] matches ROLE.id

        verify(exactly = 1) {
            commandGateway.handle(
                match<SaveRoleCommand> {
                    it.input == SaveRoleInput(
                        organizationId = STUDIO_ID,
                        role = ROLE,
                        permissions = permissions
                    )
                }
            )
        }
    }

    @Test
    fun `must return error when unauthorized`() {
        val permissions = listOf(1L, 2L, 3L)
        graphQlTester
            .documentName("role/saveRole")
            .variable("organizationId", STUDIO_ID)
            .variable("id", ROLE.id)
            .variable("name", ROLE.name)
            .variable("description", ROLE.description)
            .variable("permissions", permissions)
            .execute()
            .expectUnauthorized()
    }

    @Test
    fun `must return error when user have no required permission`() {
        val permissions = listOf(1L, 2L, 3L)
        graphQlTester
            .loginAs(USER_ID)
            .documentName("role/saveRole")
            .variable("organizationId", STUDIO_ID)
            .variable("id", ROLE.id)
            .variable("name", ROLE.name)
            .variable("description", ROLE.description)
            .variable("permissions", permissions)
            .execute()
            .expectForbidden("SaveRole")
    }
}
