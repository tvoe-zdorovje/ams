package by.anatolyloyko.ams.infrastructure.service.query

/**
 * Defines a handler for executing queries.
 *
 * Implementations of this interface process specific queries and produce results.
 *
 * @see Query
 */
interface QueryHandler {

    /**
     * Determines whether this handler can process the given query.
     *
     * @param query the query to check.
     * @return `true` if the handler can process the query, `false` otherwise.
     */
    fun canHandle(query: Query<*>): Boolean

    /**
     * Processes the given query and returns the execution result.
     *
     * @param query the query to handle.
     * @return the result of the query execution.
     */
    fun handle(query: Query<*>): Any?
}
