package by.anatolyloyko.ams.brand.graphql.resolver

import by.anatolyloyko.ams.brand.BRAND
import by.anatolyloyko.ams.brand.USER_ID
import by.anatolyloyko.ams.brand.query.GetBrandQuery
import by.anatolyloyko.ams.common.infrastructure.service.query.QueryGateway
import by.anatolyloyko.ams.commontest.graphql.GraphQLTest
import by.anatolyloyko.ams.commontest.graphql.get
import by.anatolyloyko.ams.commontest.graphql.loginAs
import by.anatolyloyko.ams.commontest.graphql.matches
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.graphql.test.tester.WebGraphQlTester

@GraphQLTest
class BrandQueriesResolverTest {
    @Autowired
    lateinit var graphQlTester: WebGraphQlTester

    @MockkBean
    lateinit var queryGateway: QueryGateway

    @Test
    fun `must return brand`() {
        every { queryGateway.handle(any<GetBrandQuery>()) } returns BRAND

        val result = graphQlTester
            .loginAs(USER_ID)
            .documentName("brand/getBrand")
            .variable("id", BRAND.id)
            .execute()

        result.errors().verify()
        val brandPath = "brands.brand"
        result["$brandPath.id"] matches BRAND.id
        result["$brandPath.name"] matches BRAND.name
        result["$brandPath.description"] matches BRAND.description
    }
}
