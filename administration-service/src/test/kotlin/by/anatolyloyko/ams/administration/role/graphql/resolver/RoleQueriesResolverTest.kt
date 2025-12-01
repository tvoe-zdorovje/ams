package by.anatolyloyko.ams.administration.role.graphql.resolver

import by.anatolyloyko.ams.administration.PERMISSION
import by.anatolyloyko.ams.administration.ROLE
import by.anatolyloyko.ams.administration.STUDIO_ID
import by.anatolyloyko.ams.administration.USER_ID
import by.anatolyloyko.ams.administration.permission.finder.PermissionFinder
import by.anatolyloyko.ams.administration.role.query.GetRoleQuery
import by.anatolyloyko.ams.administration.role.query.input.GetRoleQueryInput
import by.anatolyloyko.ams.common.infrastructure.service.query.QueryGateway
import by.anatolyloyko.ams.common.infrastructure.testing.expectForbidden
import by.anatolyloyko.ams.common.infrastructure.testing.expectUnauthorized
import by.anatolyloyko.ams.common.infrastructure.testing.get
import by.anatolyloyko.ams.common.infrastructure.testing.loginAs
import by.anatolyloyko.ams.common.infrastructure.testing.matches
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.verifyOrder
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.graphql.tester.AutoConfigureHttpGraphQlTester
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.graphql.test.tester.WebGraphQlTester

@SpringBootTest
@AutoConfigureHttpGraphQlTester
class RoleQueriesResolverTest {
    @Autowired
    lateinit var graphQlTester: WebGraphQlTester

    @MockkBean
    lateinit var queryGateway: QueryGateway

    @MockkBean
    lateinit var permissionFinder: PermissionFinder

    @Test
    fun `must return role with permissions`() {
        every { queryGateway.handle(any<GetRoleQuery>()) } returns ROLE
        every { permissionFinder.findByRoleId(any()) } returns listOf(PERMISSION)

        val result = graphQlTester
            .loginAs(USER_ID, STUDIO_ID, "GetOrganizationRole")
            .documentName("role/getRole")
            .variable("roleId", ROLE.id)
            .variable("organizationId", STUDIO_ID)
            .execute()

        result.errors().verify()
        val rolePath = "roles.role"
        result["$rolePath.id"] matches ROLE.id
        result["$rolePath.name"] matches ROLE.name
        result["$rolePath.description"] matches ROLE.description
        val permissionPath = "$rolePath.permissions[0]"
        result["$permissionPath.id"] matches PERMISSION.id
        result["$permissionPath.name"] matches PERMISSION.name
        result["$permissionPath.description"] matches PERMISSION.description

        verifyOrder {
            queryGateway.handle(
                match<GetRoleQuery> { it.input == GetRoleQueryInput(organizationId = STUDIO_ID, roleId = ROLE.id!!) }
            )

            permissionFinder.findByRoleId(ROLE.id!!)
        }
    }

    @Test
    fun `must return error when unauthorized`() {
        graphQlTester
            .documentName("role/getRole")
            .variable("roleId", ROLE.id)
            .variable("organizationId", STUDIO_ID)
            .execute()
            .expectUnauthorized()
    }

    @Test
    fun `must return error when user have no required permission`() {
        graphQlTester
            .loginAs(USER_ID)
            .documentName("role/getRole")
            .variable("roleId", ROLE.id)
            .variable("organizationId", STUDIO_ID)
            .execute()
            .expectForbidden("GetOrganizationRole")
    }
}
