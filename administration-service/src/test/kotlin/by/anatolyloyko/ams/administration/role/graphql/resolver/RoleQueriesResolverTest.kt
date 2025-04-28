package by.anatolyloyko.ams.administration.role.graphql.resolver

import by.anatolyloyko.ams.administration.ROLE
import by.anatolyloyko.ams.administration.role.query.GetRoleQuery
import by.anatolyloyko.ams.common.infrastructure.service.query.QueryGateway
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
class RoleQueriesResolverTest {
    @Autowired
    lateinit var graphQlTester: WebGraphQlTester

    @MockkBean
    lateinit var queryGateway: QueryGateway

    @Test
    fun `must return role`() {
        every { queryGateway.handle(any<GetRoleQuery>()) } returns ROLE

        val result = graphQlTester
            .documentName("role/getRole")
            .variable("id", ROLE.id)
            .execute()

        result.errors().verify()
        val rolePath = "roles.role"
        result["$rolePath.id"] matches ROLE.id
        result["$rolePath.name"] matches ROLE.name
        result["$rolePath.description"] matches ROLE.description
    }
}
