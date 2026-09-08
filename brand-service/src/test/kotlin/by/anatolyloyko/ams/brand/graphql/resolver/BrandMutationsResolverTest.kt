package by.anatolyloyko.ams.brand.graphql.resolver

import by.anatolyloyko.ams.brand.BRAND
import by.anatolyloyko.ams.brand.BRAND_ID
import by.anatolyloyko.ams.brand.USER_ID
import by.anatolyloyko.ams.brand.command.SaveBrandCommand
import by.anatolyloyko.ams.common.infrastructure.service.command.CommandGateway
import by.anatolyloyko.ams.commontest.graphql.GraphQLTest
import by.anatolyloyko.ams.commontest.graphql.expectForbidden
import by.anatolyloyko.ams.commontest.graphql.expectUnauthenticated
import by.anatolyloyko.ams.commontest.graphql.get
import by.anatolyloyko.ams.commontest.graphql.loginAs
import by.anatolyloyko.ams.commontest.graphql.matches
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.graphql.test.tester.WebGraphQlTester

@GraphQLTest
class BrandMutationsResolverTest {
    @Autowired
    lateinit var graphQlTester: WebGraphQlTester

    @MockkBean
    lateinit var commandGateway: CommandGateway

    @BeforeEach
    fun beforeEach() {
        every { commandGateway.handle(any<SaveBrandCommand>()) } returns BRAND_ID
    }

    @Nested
    inner class CreateBrandTest {
        @Test
        fun `must create brand`() {
            val result = graphQlTester
                .loginAs(USER_ID)
                .documentName("brand/createBrand")
                .variable("name", BRAND.name)
                .variable("description", BRAND.description)
                .execute()

            result.errors().verify()
            result["brands.createBrand"] matches BRAND_ID
        }

        @Test
        fun `must return error when unauthenticated`() {
            graphQlTester
                .documentName("brand/createBrand")
                .variable("name", BRAND.name)
                .variable("description", BRAND.description)
                .execute()
                .expectUnauthenticated()
        }
    }

    @Nested
    inner class UpdateBrandTest {
        @Test
        fun `must update brand`() {
            val result = graphQlTester
                .loginAs(USER_ID, BRAND.id!!, "UpdateBrand")
                .documentName("brand/updateBrand")
                .variable("organizationId", BRAND.id)
                .variable("name", BRAND.name)
                .variable("description", BRAND.description)
                .execute()

            result.errors().verify()
            result["brands.updateBrand"] matches BRAND_ID
        }

        @Test
        fun `must return error when unauthenticated`() {
            graphQlTester
                .documentName("brand/updateBrand")
                .variable("organizationId", BRAND.id)
                .variable("name", BRAND.name)
                .variable("description", BRAND.description)
                .execute()
                .expectUnauthenticated()
        }

        @Test
        fun `must return error when user have no required permission`() {
            graphQlTester
                .loginAs(USER_ID)
                .documentName("brand/updateBrand")
                .variable("organizationId", BRAND.id)
                .variable("name", BRAND.name)
                .variable("description", BRAND.description)
                .execute()
                .expectForbidden("UpdateBrand")
        }
    }
}
