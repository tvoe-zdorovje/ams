package by.anatolyloyko.ams.infrastructure.graphql

import by.anatolyloyko.ams.auth.exception.ForbiddenException
import graphql.ErrorType
import graphql.GraphQLError
import graphql.GraphqlErrorBuilder
import graphql.schema.DataFetchingEnvironment
import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter
import org.springframework.stereotype.Component

@Component
class DataFetcherExceptionResolver : DataFetcherExceptionResolverAdapter() {
    override fun resolveToSingleError(
        ex: Throwable,
        env: DataFetchingEnvironment
    ): GraphQLError? = when (ex) {
        is ForbiddenException -> GraphqlErrorBuilder
            .newError(env)
            .errorType(ErrorType.ValidationError)
            .message(ex.localizedMessage)
            .build()

        else -> super.resolveToSingleError(ex, env)
    }
}
