package by.anatolyloyko.ams.common.infrastructure.service.query

/**
 * A base class for queries that require input data.
 *
 * @param I the type of input required to execute the query.
 * @param R the type of query execution result.
 * @property input the input data required for execution.
 */
abstract class BaseQuery<I, R>(
    val input: I
) : Query<R> {
    override fun toString(): String = "${javaClass.simpleName}(input=$input)"
}
