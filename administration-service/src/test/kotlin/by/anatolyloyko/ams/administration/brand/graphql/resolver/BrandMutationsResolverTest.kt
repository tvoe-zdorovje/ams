package by.anatolyloyko.ams.administration.brand.graphql.resolver

import by.anatolyloyko.ams.administration.BRAND_ID
import by.anatolyloyko.ams.administration.NEW_ROLE
import by.anatolyloyko.ams.administration.ROLE_ID
import by.anatolyloyko.ams.administration.STUDIO_ID
import by.anatolyloyko.ams.administration.brand.command.AssignStudiosCommand
import by.anatolyloyko.ams.administration.brand.command.CreateBrandRoleCommand
import by.anatolyloyko.ams.administration.brand.command.input.AssignStudiosInput
import by.anatolyloyko.ams.administration.brand.command.input.CreateBrandRoleInput
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
class BrandMutationsResolverTest {
    @Autowired
    lateinit var graphQlTester: WebGraphQlTester

    @MockkBean
    lateinit var commandGateway: CommandGateway

    @Nested
    inner class CreateRole {
        @Test
        fun `must create role with permissions`() {
            val brandId = BRAND_ID
            val roleId = ROLE_ID
            val role = NEW_ROLE
            val permissions = listOf(1L, 2L, 3L)
            every { commandGateway.handle(any<CreateBrandRoleCommand>()) } returns roleId

            val result = graphQlTester
                .documentName("brand/createRole")
                .variable("brandId", brandId)
                .variable("name", role.name)
                .variable("description", role.description)
                .variable("permissions", permissions)
                .execute()

            result.errors().verify()
            result["brandAdm.createRole"] matches roleId

            verify(exactly = 1) {
                commandGateway.handle(
                    match<CreateBrandRoleCommand> {
                        it.input == CreateBrandRoleInput(
                            brandId = brandId,
                            role = role,
                            permissions = permissions
                        )
                    }
                )
            }
        }

        @Test
        fun `must create role without permissions`() {
            val brandId = BRAND_ID
            val roleId = ROLE_ID
            val role = NEW_ROLE
            every { commandGateway.handle(any<CreateBrandRoleCommand>()) } returns roleId

            val result = graphQlTester
                .documentName("brand/createRole")
                .variable("brandId", brandId)
                .variable("name", role.name)
                .variable("description", role.description)
                .execute()

            result.errors().verify()
            result["brandAdm.createRole"] matches roleId

            verify(exactly = 1) {
                commandGateway.handle(
                    match<CreateBrandRoleCommand> {
                        it.input == CreateBrandRoleInput(
                            brandId = brandId,
                            role = role,
                            permissions = emptyList()
                        )
                    }
                )
            }
        }
    }

    @Nested
    inner class AssignStudios {
        @Test
        fun `must assign studios to brand`() {
            val brandId = BRAND_ID
            val studios = listOf(STUDIO_ID)
            every { commandGateway.handle(any<AssignStudiosCommand>()) } returns Unit

            val result = graphQlTester
                .documentName("brand/assignStudios")
                .variable("brandId", brandId)
                .variable("studios", studios)
                .execute()

            result.errors().verify()
            result["brandAdm.assignStudios"] matches true

            verify(exactly = 1) {
                commandGateway.handle(
                    match<AssignStudiosCommand> {
                        it.input == AssignStudiosInput(
                            brandId = brandId,
                            studios = studios
                        )
                    }
                )
            }
        }

        @Test
        fun `must throw exception if studios list is empty`() {
            val brandId = BRAND_ID
            every { commandGateway.handle(any<AssignStudiosCommand>()) } returns Unit

            val result = graphQlTester
                .documentName("brand/assignStudios")
                .variable("brandId", brandId)
                .variable("studios", emptyList<Long>())
                .execute()

            result.errors().expect {
                it.message == "Parameter 'studios' must not be empty!"
            }

            verify(exactly = 0) { commandGateway.handle(any()) }
        }
    }
}
