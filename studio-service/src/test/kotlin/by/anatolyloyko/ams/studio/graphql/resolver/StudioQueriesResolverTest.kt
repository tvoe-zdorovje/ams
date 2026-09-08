package by.anatolyloyko.ams.studio.graphql.resolver

import by.anatolyloyko.ams.common.infrastructure.service.query.QueryGateway
import by.anatolyloyko.ams.commontest.graphql.GraphQLTest
import by.anatolyloyko.ams.commontest.graphql.get
import by.anatolyloyko.ams.commontest.graphql.loginAs
import by.anatolyloyko.ams.commontest.graphql.matches
import by.anatolyloyko.ams.studio.STUDIO
import by.anatolyloyko.ams.studio.USER_ID
import by.anatolyloyko.ams.studio.query.GetStudioQuery
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.graphql.test.tester.WebGraphQlTester

@GraphQLTest
class StudioQueriesResolverTest {
    @Autowired
    lateinit var graphQlTester: WebGraphQlTester

    @MockkBean
    lateinit var queryGateway: QueryGateway

    @Test
    fun `must return studio`() {
        every { queryGateway.handle(any<GetStudioQuery>()) } returns STUDIO

        val result = graphQlTester
            .loginAs(USER_ID)
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
