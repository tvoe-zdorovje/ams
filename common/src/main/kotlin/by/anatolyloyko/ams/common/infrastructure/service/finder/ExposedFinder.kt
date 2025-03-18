package by.anatolyloyko.ams.common.infrastructure.service.finder

import org.jetbrains.exposed.sql.Query
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.transactions.transaction

/**
 * Abstract class that is supposed to simplify database interactions using the Exposed library.
 *
 * Provides utility functions for fetching records and mapping query results.
 *
 * @param T a type of data being retrieved.
 *
 * ### Example
 * ```kotlin
 * internal val ENTITY_MAPPER: (ResultRow) -> Entity = {
 *     User(
 *         id = it[EntityTable.id],
 *         name = it[EntityTable.name]
 *     )
 * }
 *
 * internal class ExposedEntityFinder : EntityFinder, ExposedFinder<Entity>() {
 *     override fun findById(id: Long) = fetchSingle {
 *         EntityTable
 *             .selectAll()
 *             .where { EntityTable.id eq id }
 *     } mapUsing ENTITY_MAPPER
 *
 *     override fun findAll() = fetch {
 *         EntityTable
 *             .selectAll()
 *     } mapUsing ENTITY_MAPPER
 * }
 * ```
 */
abstract class ExposedFinder<T> {
    /**
     * Executes the provided query within a transaction and retrieves the result as a list.
     *
     * @param query a lambda returning an Exposed [Query] to be executed.
     * @return a list of [ResultRow] objects obtained from the query.
     */
    protected fun fetch(query: () -> Query): List<ResultRow> = transaction { query().toList() }

    /**
     * Executes the provided query within a transaction, limiting the result to a single record.
     *
     * @param query a lambda returning an Exposed [Query] to be executed.
     * @return the first [ResultRow] from the query result, or `null` if no records were found.
     */
    protected fun fetchSingle(query: () -> Query): ResultRow? = fetch {
        query().limit(1)
    }
        .firstOrNull()

    /**
     * Maps a nullable [ResultRow] using the provided mapping function.
     *
     * @receiver the nullable [ResultRow] to be mapped.
     * @param mapper a function transforming a [ResultRow] into an instance of [T].
     * @return the mapped object of type [T], or `null` if the input row was `null`.
     */
    protected infix fun ResultRow?.mapUsing(mapper: (ResultRow) -> T): T? = if (this == null) null else mapper(this)

    /**
     * Maps a list of [ResultRow] objects using the provided mapping function.
     *
     * Each row in the list is transformed into an instance of [T] using the mapping function.
     *
     * @receiver a list of [ResultRow] objects to be mapped.
     * @param mapper a function transforming a [ResultRow] into an instance of [T].
     * @return a list of mapped objects of type [T].
     */
    protected infix fun List<ResultRow>.mapUsing(mapper: (ResultRow) -> T): List<T> = map(mapper)
}
