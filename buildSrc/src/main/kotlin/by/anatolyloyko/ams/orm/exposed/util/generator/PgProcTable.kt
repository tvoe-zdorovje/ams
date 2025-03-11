package by.anatolyloyko.ams.orm.exposed.util.generator

import org.jetbrains.exposed.sql.CustomStringFunction
import org.jetbrains.exposed.sql.Table

/**
 * Represents the structure of the `pg_catalog.pg_proc` table in the database.
 *
 * This object provides access to metadata about  procedures and functions in the database using the Exposed library.
 */
internal object PgProcTable : Table("pg_catalog.pg_proc") {
    val name = text("proname")
    val oid = long("oid")
    val namespace = long("pronamespace")
    val type = text("prokind")

    val resultType = CustomStringFunction("pg_catalog.pg_get_function_result", oid)
    val arguments = CustomStringFunction("pg_catalog.pg_get_function_arguments", oid)
}
