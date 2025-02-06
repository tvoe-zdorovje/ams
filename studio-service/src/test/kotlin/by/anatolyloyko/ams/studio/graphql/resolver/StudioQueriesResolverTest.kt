package by.anatolyloyko.ams.studio.graphql.resolver

import by.anatolyloyko.ams.infrastructure.service.query.QueryGateway
import by.anatolyloyko.ams.infrastructure.testing.get
import by.anatolyloyko.ams.infrastructure.testing.matches
import by.anatolyloyko.ams.studio.STUDIO
import by.anatolyloyko.ams.studio.query.GetStudioQuery
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.graphql.tester.AutoConfigureHttpGraphQlTester
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.graphql.test.tester.WebGraphQlTester

@SpringBootTest
@AutoConfigureHttpGraphQlTester
class StudioQueriesResolverTest {
    @Autowired
    lateinit var graphQlTester: WebGraphQlTester

    @MockkBean
    lateinit var queryGateway: QueryGateway

    @Test
    fun `must return studio`() {
        every { queryGateway.handle(any<GetStudioQuery>()) } returns STUDIO

        val result = graphQlTester
            .documentName("studio/getStudio")
            .variable("id", STUDIO.id)
            .execute()

        result.errors().verify()
        val studioPath = "studios.studio"
        result["$studioPath.id"] matches STUDIO.id
        result["$studioPath.name"] matches STUDIO.name
        result["$studioPath.description"] matches STUDIO.description
    }
}
