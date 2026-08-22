package by.anatolyloyko.ams.administration.permission.graphql.resolver

import by.anatolyloyko.ams.administration.PERMISSION
import by.anatolyloyko.ams.administration.USER_ID
import by.anatolyloyko.ams.administration.permission.finder.PermissionFinder
import by.anatolyloyko.ams.commontest.graphql.GraphQLTest
import by.anatolyloyko.ams.commontest.graphql.get
import by.anatolyloyko.ams.commontest.graphql.loginAs
import by.anatolyloyko.ams.commontest.graphql.matches
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.graphql.test.tester.WebGraphQlTester

@GraphQLTest
class PermissionQueriesResolverTest {
    @Autowired
    lateinit var graphQlTester: WebGraphQlTester

    @MockkBean
    lateinit var permissionFinder: PermissionFinder

    @Test
    fun `must find all permissions`() {
        every { permissionFinder.findAll() } returns listOf(PERMISSION)

        val result = graphQlTester
            .loginAs(USER_ID)
            .documentName("permission/getAllPermissions")
            .execute()

        result.errors().verify()
        val permissionPath = "permissions.permissions[0]"
        result["$permissionPath.id"] matches PERMISSION.id
        result["$permissionPath.name"] matches PERMISSION.name
        result["$permissionPath.description"] matches PERMISSION.description

        verify(exactly = 1) {
            permissionFinder.findAll()
        }
    }
}
