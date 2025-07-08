package by.anatolyloyko.ams.brand.graphql.resolver

import by.anatolyloyko.ams.brand.BRAND
import by.anatolyloyko.ams.brand.BRAND_ID
import by.anatolyloyko.ams.brand.command.SaveBrandCommand
import by.anatolyloyko.ams.common.infrastructure.service.command.CommandGateway
import by.anatolyloyko.ams.common.infrastructure.testing.get
import by.anatolyloyko.ams.common.infrastructure.testing.matches
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
        every { commandGateway.handle(any<SaveBrandCommand>()) } returns BRAND_ID

        val result = graphQlTester
            .documentName("brand/createBrand")
            .variable("name", BRAND.name)
            .variable("description", BRAND.description)
            .execute()

        result.errors().verify()
        result["brands.createBrand"] matches BRAND_ID
    }

    @Test
    fun `must update brand`() {
        every { commandGateway.handle(any<SaveBrandCommand>()) } returns BRAND_ID

        val result = graphQlTester
            .documentName("brand/updateBrand")
            .variable("organizationId", BRAND.id)
            .variable("name", BRAND.name)
            .variable("description", BRAND.description)
            .execute()

        result.errors().verify()
        result["brands.updateBrand"] matches BRAND_ID
    }
}
