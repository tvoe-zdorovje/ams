package by.anatolyloyko.ams.administration.role.graphql.resolver

import by.anatolyloyko.ams.administration.PERMISSION
import by.anatolyloyko.ams.administration.ROLE
import by.anatolyloyko.ams.administration.permission.finder.PermissionFinder
import by.anatolyloyko.ams.administration.role.query.GetRoleQuery
import by.anatolyloyko.ams.common.infrastructure.service.query.QueryGateway
import by.anatolyloyko.ams.common.infrastructure.testing.get
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
            .documentName("role/getRole")
            .variable("id", ROLE.id)
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
                match<GetRoleQuery> { it.input == ROLE.id }
            )

            permissionFinder.findByRoleId(ROLE.id!!)
        }
    }
}
