package by.anatolyloyko.ams.administration.role.graphql.resolver

import by.anatolyloyko.ams.administration.ROLE
import by.anatolyloyko.ams.administration.role.command.SaveRoleCommand
import by.anatolyloyko.ams.administration.role.command.input.SaveRoleInput
import by.anatolyloyko.ams.common.infrastructure.service.command.CommandGateway
import by.anatolyloyko.ams.common.infrastructure.testing.get
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
    fun `must create role`() {
        val permissions = listOf(1L, 2L, 3L)
        every { commandGateway.handle(any<SaveRoleCommand>()) } returns ROLE.id!!

        val result = graphQlTester
            .documentName("role/saveRole")
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
                        role = ROLE,
                        permissions = permissions
                    )
                }
            )
        }
    }
}
