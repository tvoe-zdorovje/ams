package by.anatolyloyko.ams.brand.graphql.resolver

import by.anatolyloyko.ams.brand.BRAND
import by.anatolyloyko.ams.brand.BRAND_ID
import by.anatolyloyko.ams.brand.command.CreateBrandCommand
import by.anatolyloyko.ams.infrastructure.service.command.CommandGateway
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
class BrandMutationsResolverTest {
    @Autowired
    lateinit var graphQlTester: WebGraphQlTester

    @MockkBean
    lateinit var commandGateway: CommandGateway

    @Test
    fun `must create brand`() {
        every { commandGateway.handle(any<CreateBrandCommand>()) } returns BRAND_ID

        val result = graphQlTester
            .documentName("brand/createBrand")
            .variable("name", BRAND.name)
            .variable("description", BRAND.description)
            .execute()

        result.errors().verify()
        result["brands.createBrand"] matches BRAND_ID
    }
}
