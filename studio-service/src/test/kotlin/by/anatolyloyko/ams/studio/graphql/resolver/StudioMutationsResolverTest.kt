package by.anatolyloyko.ams.studio.graphql.resolver

import by.anatolyloyko.ams.common.infrastructure.service.command.CommandGateway
import by.anatolyloyko.ams.commontest.graphql.GraphQLTest
import by.anatolyloyko.ams.commontest.graphql.expectForbidden
import by.anatolyloyko.ams.commontest.graphql.expectUnauthenticated
import by.anatolyloyko.ams.commontest.graphql.get
import by.anatolyloyko.ams.commontest.graphql.loginAs
import by.anatolyloyko.ams.commontest.graphql.matches
import by.anatolyloyko.ams.studio.BRAND_ID
import by.anatolyloyko.ams.studio.STUDIO
import by.anatolyloyko.ams.studio.STUDIO_ID
import by.anatolyloyko.ams.studio.USER_ID
import by.anatolyloyko.ams.studio.command.SaveStudioCommand
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.graphql.test.tester.WebGraphQlTester

@GraphQLTest
class StudioMutationsResolverTest {
    @Autowired
    lateinit var graphQlTester: WebGraphQlTester

    @MockkBean
    lateinit var commandGateway: CommandGateway

    @BeforeEach
    fun beforeEach() {
        every { commandGateway.handle(any<SaveStudioCommand>()) } returns STUDIO_ID
    }

    @Nested
    inner class CreateStudioTest {
        @Test
        fun `must create studio`() {
            val result = graphQlTester
                .loginAs(USER_ID)
                .documentName("studio/createStudio")
                .variable("organizationId", BRAND_ID)
                .variable("name", STUDIO.name)
                .variable("description", STUDIO.description)
                .execute()

            result.errors().verify()
            result["studios.createStudio"] matches STUDIO_ID
        }

        @Test
        fun `must return error when unauthenticated`() {
            graphQlTester
                .documentName("studio/createStudio")
                .variable("organizationId", BRAND_ID)
                .variable("name", STUDIO.name)
                .variable("description", STUDIO.description)
                .execute()
                .expectUnauthenticated()
        }
    }

    @Nested
    inner class UpdateStudioTest {
        @Test
        fun `must update studio`() {
            val result = graphQlTester
                .loginAs(USER_ID, STUDIO.id!!, "UpdateStudio")
                .documentName("studio/updateStudio")
                .variable("organizationId", STUDIO.id)
                .variable("name", STUDIO.name)
                .variable("description", STUDIO.description)
                .execute()

            result.errors().verify()
            result["studios.updateStudio"] matches STUDIO_ID
        }

        @Test
        fun `must return error when unauthenticated`() {
            graphQlTester
                .documentName("studio/updateStudio")
                .variable("organizationId", STUDIO.id)
                .variable("name", STUDIO.name)
                .variable("description", STUDIO.description)
                .execute()
                .expectUnauthenticated()
        }

        @Test
        fun `must return error when user have no required permission`() {
            graphQlTester
                .loginAs(USER_ID)
                .documentName("studio/updateStudio")
                .variable("organizationId", STUDIO.id)
                .variable("name", STUDIO.name)
                .variable("description", STUDIO.description)
                .execute()
                .expectForbidden("UpdateStudio")
        }
    }
}
