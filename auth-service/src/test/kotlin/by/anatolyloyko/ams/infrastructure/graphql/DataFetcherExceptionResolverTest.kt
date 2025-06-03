package by.anatolyloyko.ams.infrastructure.graphql

import by.anatolyloyko.ams.auth.exception.ForbiddenException
import graphql.ErrorType
import graphql.schema.DataFetchingEnvironment
import io.mockk.mockk
import org.assertj.core.api.WithAssertions
import org.junit.jupiter.api.Test

class DataFetcherExceptionResolverTest : DataFetcherExceptionResolver(), WithAssertions {
    private val dataFetchingEnvironment = mockk<DataFetchingEnvironment>(relaxed = true)

    @Test
    fun `must handle ForbiddenException`() {
        val exception = ForbiddenException("message")

        val result = resolveToSingleError(exception, dataFetchingEnvironment)!!

        assertThat(result.errorType).isEqualTo(ErrorType.ValidationError)
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