package by.anatolyloyko.ams.studio.graphql.resolver

import by.anatolyloyko.ams.common.infrastructure.service.command.CommandGateway
import by.anatolyloyko.ams.common.infrastructure.testing.get
import by.anatolyloyko.ams.common.infrastructure.testing.matches
import by.anatolyloyko.ams.studio.STUDIO
import by.anatolyloyko.ams.studio.STUDIO_ID
import by.anatolyloyko.ams.studio.command.SaveStudioCommand
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
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

    @Test
    fun `must create studio`() {
        every { commandGateway.handle(any<SaveStudioCommand>()) } returns STUDIO_ID

        val result = graphQlTester
            .documentName("studio/createStudio")
            .variable("name", STUDIO.name)
            .variable("description", STUDIO.description)
            .execute()

        result.errors().verify()
        result["studios.createStudio"] matches STUDIO_ID
    }

    @Test
    fun `must update studio`() {
        every { commandGateway.handle(any<SaveStudioCommand>()) } returns STUDIO_ID

        val result = graphQlTester
            .documentName("studio/updateStudio")
            .variable("organizationId", STUDIO.id)
            .variable("name", STUDIO.name)
            .variable("description", STUDIO.description)
            .execute()

        result.errors().verify()
        result["studios.updateStudio"] matches STUDIO_ID
    }
}
