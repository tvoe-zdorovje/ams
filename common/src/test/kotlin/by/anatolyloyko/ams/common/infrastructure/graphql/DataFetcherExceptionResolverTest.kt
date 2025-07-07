package by.anatolyloyko.ams.common.infrastructure.graphql

import by.anatolyloyko.ams.common.infrastructure.exception.AuthenticationException
import graphql.ErrorType.ValidationError
import graphql.schema.DataFetchingEnvironment
import io.mockk.mockk
import org.assertj.core.api.WithAssertions
import org.junit.jupiter.api.Test
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
        val exception = AuthenticationException("message")

        val result = resolveToSingleError(exception, dataFetchingEnvironment)!!

        assertThat(result.errorType).isEqualTo(UNAUTHORIZED)
        assertThat(result.message).isEqualTo(exception.localizedMessage)
    }

    @Test
    fun `must call super if no suitable handlers found`() {
        val exception = Throwable("message")
        val exceptedResult = super.resolveToSingleError(exception, dataFetchingEnvironment)

        val result = resolveToSingleError(exception, dataFetchingEnvironment)

        assertThat(result).isEqualTo(exceptedResult)
    }
}
