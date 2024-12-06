package by.anatolyloyko.ams.infrastructure.service.query

/**
 * A gateway for dispatching queries to their appropriate handlers.
 *
 * The gateway finds a suitable query handler and delegates execution,
 * returning the result produced by the handler.
 *
 * @see Query
 * @see QueryHandler
 */
interface QueryGateway {
    /**
     * Dispatches the given query to an appropriate handler for execution.
     *
     * @param R the query execution result type.
     * @param query the query to handle.
     * @return the query execution result.
     * @throws IllegalStateException if no suitable handler is found.
     */
    fun <R> handle(query: Query<R>): R
}
