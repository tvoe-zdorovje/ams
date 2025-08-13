package by.anatolyloyko.ams.administration.user.graphql.resolver

import by.anatolyloyko.ams.administration.BRAND_ID
import by.anatolyloyko.ams.administration.ROLE
import by.anatolyloyko.ams.administration.USER_ID
import by.anatolyloyko.ams.administration.user.query.GetUserRolesQuery
import by.anatolyloyko.ams.administration.user.query.input.GetUserRolesQueryInput
import by.anatolyloyko.ams.common.infrastructure.service.query.QueryGateway
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
class UserQueriesResolverTest {
    @Autowired
    lateinit var graphQlTester: WebGraphQlTester

    @MockkBean(relaxed = true)
    lateinit var queryGateway: QueryGateway

    @Nested
    inner class GetRoles {
        @Test
        fun `must return user roles`() {
            every { queryGateway.handle(any<GetUserRolesQuery>()) } returns listOf(ROLE)

            val userId = USER_ID
            val organizationId = BRAND_ID

            val result = graphQlTester
                .documentName("user/getRoles")
                .variable("userId", userId)
                .variable("organizationId", organizationId)
                .execute()

            result.errors().verify()
            val rolePath = "users.roles[0]"
            result["$rolePath.id"] matches ROLE.id
            result["$rolePath.name"] matches ROLE.name
            result["$rolePath.description"] matches ROLE.description

            verify(exactly = 1) {
                queryGateway.handle(
                    match<GetUserRolesQuery> {
                        it.input == GetUserRolesQueryInput(
                            userId = userId,
                            organizationId = organizationId,
                        )
                    }
                )
            }
        }
    }
}
