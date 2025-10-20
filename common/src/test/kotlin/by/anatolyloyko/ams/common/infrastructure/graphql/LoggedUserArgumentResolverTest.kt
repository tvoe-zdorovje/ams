package by.anatolyloyko.ams.common.infrastructure.graphql

import by.anatolyloyko.ams.common.infrastructure.exception.AuthorizationException
import by.anatolyloyko.ams.common.infrastructure.graphql.auth.CONTEXT_LOGGED_USER
import by.anatolyloyko.ams.common.infrastructure.graphql.auth.LoggedUserArgumentResolver
import by.anatolyloyko.ams.common.infrastructure.graphql.auth.Principal
import by.anatolyloyko.ams.common.infrastructure.graphql.auth.model.LoggedUser
import graphql.schema.DataFetchingEnvironment
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.WithAssertions
import org.junit.jupiter.api.Test
import org.springframework.core.MethodParameter

class LoggedUserArgumentResolverTest : WithAssertions {
    private val resolver = LoggedUserArgumentResolver()

    private val methodParameter = mockk<MethodParameter>()

    @Test
    fun `must support parameter annotated with Principal annotation`() {
        every { methodParameter.hasParameterAnnotation(Principal::class.java) } returns true

        assertThat(resolver.supportsParameter(methodParameter)).isTrue()

        every { methodParameter.hasParameterAnnotation(Principal::class.java) } returns false

        assertThat(resolver.supportsParameter(methodParameter)).isFalse()
    }

    @Test
    fun `must resolve argument by graphQl Context`() {
        val loggedUser = LoggedUser(123)
        val environment = mockk<DataFetchingEnvironment> {
            every { graphQlContext.get<Any>(CONTEXT_LOGGED_USER) } returns loggedUser
        }

        val result = resolver.resolveArgument(methodParameter, environment)

        assertThat(result).isEqualTo(loggedUser)
    }

    @Test
    fun `must throw exception when context has no value`() {
        val environment = mockk<DataFetchingEnvironment> {
            every { graphQlContext.get<Any>(CONTEXT_LOGGED_USER) } returns null
        }

        assertThatThrownBy { resolver.resolveArgument(methodParameter, environment) }
            .isInstanceOf(AuthorizationException::class.java)
            .hasMessage("Authentication required")
    }
}
