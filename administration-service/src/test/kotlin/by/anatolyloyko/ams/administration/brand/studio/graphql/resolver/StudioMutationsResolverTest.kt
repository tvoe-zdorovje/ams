package by.anatolyloyko.ams.administration.brand.studio.graphql.resolver

import by.anatolyloyko.ams.administration.NEW_ROLE
import by.anatolyloyko.ams.administration.ROLE_ID
import by.anatolyloyko.ams.administration.STUDIO_ID
import by.anatolyloyko.ams.administration.brand.studio.command.CreateStudioRoleCommand
import by.anatolyloyko.ams.administration.brand.studio.command.input.CreateStudioRoleInput
import by.anatolyloyko.ams.common.infrastructure.service.command.CommandGateway
import by.anatolyloyko.ams.common.infrastructure.testing.get
import by.anatolyloyko.ams.common.infrastructure.testing.matches
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.verify
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.graphql.tester.AutoConfigureHttpGraphQlTester
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.graphql.test.tester.WebGraphQlTester

@SpringBootTest
@AutoConfigureHttpGraphQlTester
class StudioMutationsResolverTest {
    @Autowired
    lateinit var graphQlTester: WebGraphQlTester

    @MockkBean
    lateinit var commandGateway: CommandGateway

    @Nested
    inner class CreateRole {
        @Test
        fun `must create role with permissions`() {
            val studioId = STUDIO_ID
            val roleId = ROLE_ID
            val role = NEW_ROLE
            val permissions = listOf(1L, 2L, 3L)
            every { commandGateway.handle(any<CreateStudioRoleCommand>()) } returns roleId

            val result = graphQlTester
                .documentName("brand/studio/createRole")
                .variable("studioId", studioId)
                .variable("name", role.name)
                .variable("description", role.description)
                .variable("permissions", permissions)
                .execute()

            result.errors().verify()
            result["studios.createRole"] matches roleId

            verify(exactly = 1) {
                commandGateway.handle(
                    match<CreateStudioRoleCommand> {
                        it.input == CreateStudioRoleInput(
                            studioId = studioId,
                            role = role,
                            permissions = permissions
                        )
                    }
                )
            }
        }

        @Test
        fun `must create role without permissions`() {
            val studioId = STUDIO_ID
            val roleId = ROLE_ID
            val role = NEW_ROLE
            every { commandGateway.handle(any<CreateStudioRoleCommand>()) } returns roleId

            val result = graphQlTester
                .documentName("brand/studio/createRole")
                .variable("studioId", studioId)
                .variable("name", role.name)
                .variable("description", role.description)
                .execute()

            result.errors().verify()
            result["studios.createRole"] matches roleId

            verify(exactly = 1) {
                commandGateway.handle(
                    match<CreateStudioRoleCommand> {
                        it.input == CreateStudioRoleInput(
                            studioId = studioId,
                            role = role,
                            permissions = emptyList()
                        )
                    }
                )
            }
        }
    }
}
