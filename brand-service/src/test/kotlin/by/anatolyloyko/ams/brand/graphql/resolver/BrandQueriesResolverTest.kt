package by.anatolyloyko.ams.brand.graphql.resolver

import by.anatolyloyko.ams.brand.BRAND
import by.anatolyloyko.ams.brand.query.GetBrandQuery
import by.anatolyloyko.ams.infrastructure.service.query.QueryGateway
import by.anatolyloyko.ams.infrastructure.testing.get
import by.anatolyloyko.ams.infrastructure.testing.matches
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.graphql.tester.AutoConfigureHttpGraphQlTester
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.graphql.test.tester.WebGraphQlTester

@SpringBootTest
@AutoConfigureHttpGraphQlTester
class BrandQueriesResolverTest {
    @Autowired
    lateinit var graphQlTester: WebGraphQlTester

    @MockkBean
    lateinit var queryGateway: QueryGateway

    @Test
    fun `must return brand`() {
        every { queryGateway.handle(any<GetBrandQuery>()) } returns BRAND

        val result = graphQlTester
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
