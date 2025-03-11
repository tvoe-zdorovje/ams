package by.anatolyloyko.ams.orm.exposed.util.generator

import org.jetbrains.exposed.sql.Table

/**
 * Represents the structure of the `pg_catalog.namespace` table in the database.
 *
 * This object provides access to metadata about namespaces in the database using the Exposed library.
 */
internal object PgNamespaceTable : Table("pg_catalog.pg_namespace") {
    val name = text("nspname")
    val oid = long("oid")
}
