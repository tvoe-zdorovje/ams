package by.anatolyloyko.ams.common.infrastructure.graphql

import graphql.ErrorType.ValidationError
import graphql.GraphQLError
import graphql.GraphqlErrorBuilder
import graphql.schema.DataFetchingEnvironment
import org.jooq.exception.DataAccessException
import org.postgresql.util.PSQLException
import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter
import org.springframework.graphql.execution.ErrorType
import org.springframework.security.access.AccessDeniedException
import org.springframework.stereotype.Component

@Component
class DataFetcherExceptionResolver : DataFetcherExceptionResolverAdapter() {
    override fun resolveToSingleError(
        ex: Throwable,
        env: DataFetchingEnvironment
    ): GraphQLError? = when (ex) {
        is IllegalStateException -> GraphqlErrorBuilder
            .newError(env)
            .errorType(ValidationError)
            .message(ex.localizedMessage)
            .build()
        is AccessDeniedException -> GraphqlErrorBuilder
            .newError(env)
            .errorType(ErrorType.FORBIDDEN)
            .message("Access denied: ${ex.localizedMessage}")
            .build()
        is DataAccessException -> ex.resolveToSingleError(env)

        else -> super.resolveToSingleError(ex, env)
    }

    private fun DataAccessException.resolveToSingleError(env: DataFetchingEnvironment): GraphQLError? {
        if (cause == null || cause !is PSQLException) {
            return super.resolveToSingleError(this, env)
        }

        return GraphqlErrorBuilder
            .newError(env)
            .errorType(ValidationError)
            .message((cause as PSQLException).message?.substringBefore('\n') ?: ValidationError.name)
            .build()
    }
}
