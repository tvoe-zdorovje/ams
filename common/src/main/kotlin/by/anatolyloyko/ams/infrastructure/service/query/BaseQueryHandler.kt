package by.anatolyloyko.ams.infrastructure.service.query

import by.anatolyloyko.ams.infrastructure.logging.log
import java.lang.reflect.ParameterizedType

/**
 * A base class for query handlers that process specific query types.
 *
 * This class provides automatic type detection and logging for query execution.
 *
 * @param I the type of query handled by this handler.
 * @param R the query execution result type.
 */
abstract class BaseQueryHandler<I : Query<R>, R : Any?> : QueryHandler {
    private val queryType = checkNotNull(
        (this.javaClass.genericSuperclass as? ParameterizedType)
            ?.actualTypeArguments
            ?.get(0)
    ) { "Cannot detect query class" }

    /**
     * Checks if this handler can process the given query by comparing
     * the type of the provided query with the parameterized type {@code I}.
     *
     * @param query the query to check.
     * @return `true` if the handler can process the query, `false` otherwise.
     */
    override fun canHandle(query: Query<*>): Boolean = queryType == query.javaClass

    /**
     * Processes the given query and returns the execution result.
     *
     * Logs the query execution process before and after calling {@link #handleInternal}.
     *
     * @param query the query to handle.
     * @return the result of the query execution.
     */
    override fun handle(query: Query<*>): R {
        log.info("Handling query $query")
        @Suppress("UNCHECKED_CAST")
        val result = handleInternal(query as I)
        log.info("Handling query $query completed.\nResult: $result")

        return result
    }

    /**
     * Executes the query-specific logic.
     *
     * @param query the query to process.
     * @return the command execution result.
     */
    protected abstract fun handleInternal(query: I): R
}
