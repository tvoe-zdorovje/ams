package by.anatolyloyko.ams.administration.permission.graphql.resolver

import by.anatolyloyko.ams.administration.PERMISSION
import by.anatolyloyko.ams.administration.permission.finder.PermissionFinder
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
class PermissionQueriesResolverTest {
    @Autowired
    lateinit var graphQlTester: WebGraphQlTester

    @MockkBean
    lateinit var permissionFinder: PermissionFinder

    @Test
    fun `must find all permissions`() {
        every { permissionFinder.findAll() } returns listOf(PERMISSION)

        val result = graphQlTester
            .documentName("permission/getAllPermissions")
            .execute()

        result.errors().verify()
        val permissionPath = "permissions.permissions[0]"
        result["$permissionPath.id"] matches PERMISSION.id
        result["$permissionPath.name"] matches PERMISSION.name
        result["$permissionPath.description"] matches PERMISSION.description
    }
}
