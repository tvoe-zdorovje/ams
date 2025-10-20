package by.anatolyloyko.ams.common.infrastructure.graphql

import by.anatolyloyko.ams.common.infrastructure.exception.AccessDeniedException
import by.anatolyloyko.ams.common.infrastructure.exception.AuthorizationException
import graphql.ErrorType.ValidationError
import graphql.schema.DataFetchingEnvironment
import io.mockk.mockk
import org.assertj.core.api.WithAssertions
import org.jooq.exception.DataAccessException
import org.junit.jupiter.api.Test
import org.postgresql.util.PSQLException
import org.postgresql.util.PSQLState
import org.springframework.graphql.execution.ErrorType.FORBIDDEN
import org.springframework.graphql.execution.ErrorType.UNAUTHORIZED

class DataFetcherExceptionResolverTest : DataFetcherExceptionResolver(), WithAssertions {
    private val dataFetchingEnvironment = mockk<DataFetchingEnvironment>(relaxed = true)

    @Test
    fun `must handle IllegalStateException`() {
        val exception = IllegalStateException("message")

        val result = resolveToSingleError(exception, dataFetchingEnvironment)!!

        assertThat(result.errorType).isEqualTo(ValidationError)
        assertThat(result.message).isEqualTo(exception.localizedMessage)
    }

    @Test
    fun `must handle AuthenticationException`() {
        val exception = AuthorizationException("message")

        val result = resolveToSingleError(exception, dataFetchingEnvironment)!!

        assertThat(result.errorType).isEqualTo(UNAUTHORIZED)
        assertThat(result.message).isEqualTo(exception.localizedMessage)
    }

    @Test
    fun `must handle AccessDeniedException`() {
        val exception = AccessDeniedException("message")

        val result = resolveToSingleError(exception, dataFetchingEnvironment)!!

        assertThat(result.errorType).isEqualTo(FORBIDDEN)
        assertThat(result.message).isEqualTo(exception.localizedMessage)
    }

    @Test
    fun `must handle DataAccessException`() {
        var exception = DataAccessException("message")

        var result = resolveToSingleError(exception, dataFetchingEnvironment)

        val exceptedResult = super.resolveToSingleError(exception, dataFetchingEnvironment)
        assertThat(result).isEqualTo(exceptedResult)

        // PSQL exception

        exception = DataAccessException("message", PSQLException(null, PSQLState.INVALID_PARAMETER_TYPE))

        result = resolveToSingleError(exception, dataFetchingEnvironment)!!

        assertThat(result.errorType).isEqualTo(ValidationError)
        assertThat(result.message).isEqualTo(ValidationError.name)

        exception = DataAccessException("message", PSQLException("psql-msg", PSQLState.INVALID_PARAMETER_TYPE))

        result = resolveToSingleError(exception, dataFetchingEnvironment)!!

        assertThat(result.errorType).isEqualTo(ValidationError)
        assertThat(result.message).isEqualTo("psql-msg")
    }

    @Test
    fun `must call super if no suitable handlers found`() {
        val exception = Throwable("message")
        val exceptedResult = super.resolveToSingleError(exception, dataFetchingEnvironment)

        val result = resolveToSingleError(exception, dataFetchingEnvironment)

        assertThat(result).isEqualTo(exceptedResult)
    }
}
