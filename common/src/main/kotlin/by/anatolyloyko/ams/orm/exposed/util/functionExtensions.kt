package by.anatolyloyko.ams.orm.exposed.util

import org.jetbrains.exposed.sql.CustomFunction
import org.jetbrains.exposed.sql.QueryBuilder
import org.jetbrains.exposed.sql.transactions.transaction

fun <T> CustomFunction<T>.select(): T = checkNotNull(select(false))

fun <T> CustomFunction<T>.select(isResultNullable: Boolean): T? = transaction {
    exec(
        QueryBuilder(false)
            .append("SELECT * FROM ")
            .apply(::toQueryBuilder)
            .toString()
    ) {
        while (it.next()) {
            return@exec columnType.valueFromDB(it.getObject(1)) ?: continue
        }

        check(isResultNullable) { "Execution result is null" }

        null
    }
}
