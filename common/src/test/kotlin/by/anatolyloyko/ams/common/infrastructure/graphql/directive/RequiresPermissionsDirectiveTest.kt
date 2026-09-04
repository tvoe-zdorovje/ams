package by.anatolyloyko.ams.common.infrastructure.graphql.directive

import by.anatolyloyko.ams.common.infrastructure.graphql.auth.CONTEXT_AUTHENTICATION
import graphql.language.ArrayValue
import graphql.language.StringValue
import graphql.schema.DataFetcher
import graphql.schema.DataFetchingEnvironment
import graphql.schema.GraphQLAppliedDirective
import graphql.schema.GraphQLAppliedDirectiveArgument
import graphql.schema.GraphQLFieldDefinition
import graphql.schema.idl.SchemaDirectiveWiringEnvironment
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import org.assertj.core.api.WithAssertions
import org.junit.jupiter.api.Test
import org.springframework.security.authentication.InternalAuthenticationServiceException
import org.springframework.security.authorization.AuthorizationDeniedException
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken

private const val ORGANIZATION_ID = 42L

private const val REQUIRED_PERMISSION = "requiredPermission"

class RequiresPermissionsDirectiveTest : WithAssertions {
    private val directive = RequiresPermissionsDirective()

    private val graphQLFieldDefinition = mockk<GraphQLFieldDefinition>()

    @Test
    fun `must return original data fetcher result when permissions exist`() {
        val expectedResult = "dataFetcherResult"
        val dataFetcher = directive.wireField(requiredPermissions = listOf(REQUIRED_PERMISSION)) {
            expectedResult
        }

        val result = dataFetcher.get(
            environment(
                permissions = setOf(REQUIRED_PERMISSION),
                arguments = mapOf("request" to mapOf(INPUT_ARGUMENT_NAME_ORGANIZATION_ID to ORGANIZATION_ID))
            )
        )

        assertThat(result).isEqualTo(expectedResult)
    }

    @Test
    fun `must use input argument to resolve organization id`() {
        val expectedResult = "dataFetcherResult"
        val dataFetcher = directive.wireField(requiredPermissions = listOf(REQUIRED_PERMISSION)) {
            expectedResult
        }

        val result = dataFetcher.get(
            environment(
                permissions = setOf(REQUIRED_PERMISSION),
                arguments = mapOf("input" to mapOf(INPUT_ARGUMENT_NAME_ORGANIZATION_ID to ORGANIZATION_ID))
            )
        )

        assertThat(result).isEqualTo(expectedResult)
    }

    @Test
    fun `must use payload argument to resolve organization id`() {
        val expectedResult = "dataFetcherResult"
        val dataFetcher = directive.wireField(requiredPermissions = listOf(REQUIRED_PERMISSION)) {
            expectedResult
        }

        val result = dataFetcher.get(
            environment(
                permissions = setOf(REQUIRED_PERMISSION),
                arguments = mapOf("payload" to mapOf(INPUT_ARGUMENT_NAME_ORGANIZATION_ID to ORGANIZATION_ID))
            )
        )

        assertThat(result).isEqualTo(expectedResult)
    }

    @Test
    fun `must throw exception when permissions are insufficient`() {
        val dataFetcher = directive.wireField(requiredPermissions = listOf(REQUIRED_PERMISSION))

        assertThatThrownBy {
            dataFetcher.get(
                environment(
                    arguments = mapOf("request" to mapOf(INPUT_ARGUMENT_NAME_ORGANIZATION_ID to ORGANIZATION_ID))
                )
            )
        }
            .isInstanceOf(AuthorizationDeniedException::class.java)
            .hasMessage("insufficient permissions - [$REQUIRED_PERMISSION].")
    }

    @Test
    fun `must throw exception when organization ID argument is missing`() {
        val dataFetcher = directive.wireField(requiredPermissions = listOf(REQUIRED_PERMISSION))

        assertThatThrownBy {
            dataFetcher.get(
                environment(
                    permissions = setOf(REQUIRED_PERMISSION),
                    arguments = mapOf("request" to emptyMap())
                )
            )
        }
            .isInstanceOf(AuthorizationDeniedException::class.java)
            .hasMessage("'$INPUT_ARGUMENT_NAME_ORGANIZATION_ID' argument is missed or invalid.")
    }

    @Test
    fun `must throw exception when organization ID argument is invalid`() {
        val dataFetcher = directive.wireField(requiredPermissions = listOf(REQUIRED_PERMISSION))

        assertThatThrownBy {
            dataFetcher.get(
                environment(
                    permissions = setOf(REQUIRED_PERMISSION),
                    arguments = mapOf("request" to mapOf(INPUT_ARGUMENT_NAME_ORGANIZATION_ID to "invalid"))
                )
            )
        }
            .isInstanceOf(AuthorizationDeniedException::class.java)
            .hasMessage("'$INPUT_ARGUMENT_NAME_ORGANIZATION_ID' argument is missed or invalid.")
    }

    @Test
    fun `must throw exception when authentication is missing`() {
        val dataFetcher = directive.wireField(requiredPermissions = listOf(REQUIRED_PERMISSION))

        assertThatThrownBy {
            dataFetcher.get(
                environment(
                    authentication = null,
                    arguments = mapOf("request" to mapOf(INPUT_ARGUMENT_NAME_ORGANIZATION_ID to ORGANIZATION_ID))
                )
            )
        }
            .isInstanceOf(InternalAuthenticationServiceException::class.java)
            .hasMessage("GraphQL context doesn't contain a valid authentication.")
    }

    private fun RequiresPermissionsDirective.wireField(
        requiredPermissions: List<String>,
        originalDataFetcher: DataFetcher<Any?> = DataFetcher { null }
    ): DataFetcher<Any?> {
        val wiredDataFetcher = slot<DataFetcher<Any?>>()
        val environment = mockk<SchemaDirectiveWiringEnvironment<GraphQLFieldDefinition>> {
            every { fieldDataFetcher } returns originalDataFetcher
            every { element } returns graphQLFieldDefinition
            every { setFieldDataFetcher(capture(wiredDataFetcher)) } returns graphQLFieldDefinition
        }
        every {
            graphQLFieldDefinition.getAppliedDirective(DIRECTIVE_NAME)
        } returns appliedDirective(requiredPermissions)

        onField(environment)

        return wiredDataFetcher.captured
    }

    private fun appliedDirective(requiredPermissions: List<String>): GraphQLAppliedDirective {
        val argument = mockk<GraphQLAppliedDirectiveArgument> {
            every { argumentValue.value } returns ArrayValue(requiredPermissions.map { StringValue(it) })
        }
        return mockk {
            every { getArgument(DIRECTIVE_ARGUMENT_NAME) } returns argument
        }
    }

    private fun environment(
        permissions: Set<String>? = null,
        authentication: JwtAuthenticationToken? = jwtAuthenticationToken(permissions),
        arguments: Map<String, Map<String, Any?>>
    ) = mockk<DataFetchingEnvironment> {
        every { graphQlContext.get<JwtAuthenticationToken>(CONTEXT_AUTHENTICATION) } returns authentication
        every { getArgument<Map<String, Any?>>("request") } returns arguments["request"]
        every { getArgument<Map<String, Any?>>("input") } returns arguments["input"]
        every { getArgument<Map<String, Any?>>("payload") } returns arguments["payload"]
    }

    private fun jwtAuthenticationToken(permissions: Set<String>?): JwtAuthenticationToken {
        val jwt = Jwt
            .withTokenValue("token")
            .header("alg", "none")
            .claim(JWT_PERMISSIONS_CLAIM, mapOf(ORGANIZATION_ID.toString() to permissions))
            .build()
        return JwtAuthenticationToken(jwt)
    }
}
