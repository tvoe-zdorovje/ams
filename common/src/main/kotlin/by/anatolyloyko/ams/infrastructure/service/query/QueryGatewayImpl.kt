package by.anatolyloyko.ams.infrastructure.service.query

import by.anatolyloyko.ams.infrastructure.logging.log
import by.anatolyloyko.ams.infrastructure.service.command.Command
import by.anatolyloyko.ams.infrastructure.service.command.CommandHandler
import org.springframework.stereotype.Service
import java.util.concurrent.ConcurrentHashMap

/**
 * Implementation of {@link QueryGateway}.
 *
 * This implementation maintains a cache of query handlers and ensures
 * that each query is dispatched to the appropriate handler for execution.
 *
 * @param queryHandlers the collection of available query handlers.
 *
 * @see Query
 * @see QueryHandler
 */
@Service
class QueryGatewayImpl(
    private val queryHandlers: Collection<QueryHandler>
) : QueryGateway {
    private val queryHandlersMap = ConcurrentHashMap<Class<Query<*>>, QueryHandler>()

    /**
     * Dispatches the given query to the appropriate handler and returns the result.
     *
     * The handler is determined dynamically and cached for future calls.
     *
     * @param R the query execution result.
     * @param query the query to handle.
     * @return the query execution result.
     * @throws IllegalStateException if no suitable handler is found.
     */
    @Suppress("UNCHECKED_CAST")
    override fun <R> handle(query: Query<R>): R = findQueryHandler(query).handle(query) as R

    /**
     * Finds a suitable handler for the given query.
     *
     * If no handler is found, an error is logged and an exception is thrown.
     *
     * @param query the query to find a handler for.
     * @return the query handler capable of processing the query.
     * @throws IllegalStateException if no handler is found.
     *
     * @see QueryHandler.canHandle
     */
    private fun findQueryHandler(query: Query<*>) = try {
        queryHandlersMap.computeIfAbsent(query.javaClass) {
            queryHandlers.single { it.canHandle(query) }
        }
    } catch (e: Exception) {
        val message = "Cannot find query handler for $query"
        log.error(message, e)
        throw IllegalStateException(message, e)
    }
}
