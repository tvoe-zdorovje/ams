package by.anatolyloyko.ams.user.graphql.resolver

import by.anatolyloyko.ams.common.infrastructure.service.query.QueryGateway
import by.anatolyloyko.ams.common.infrastructure.testing.get
import by.anatolyloyko.ams.common.infrastructure.testing.matches
import by.anatolyloyko.ams.user.USER
import by.anatolyloyko.ams.user.query.GetUserQuery
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
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

    @MockkBean
    lateinit var queryGateway: QueryGateway

    @Test
    fun `must return user`() {
        every { queryGateway.handle(any<GetUserQuery>()) } returns USER

        val result = graphQlTester
            .documentName("user/getUser")
            .variable("id", USER.id)
            .execute()

        result.errors().verify()
        val userPath = "users.user"
        result["$userPath.id"] matches USER.id
        result["$userPath.firstName"] matches USER.firstName
        result["$userPath.lastName"] matches USER.lastName
        result["$userPath.phoneNumber"] matches USER.phoneNumber
    }
}
