package by.anatolyloyko.ams.orm.exposed.util.generator

import org.jetbrains.exposed.sql.Table

/**
 * Represents the structure of the `information_schema.columns` table in the database.
 *
 * This object provides access to metadata about the columns of tables in the database using the Exposed library.
 */
internal object TableColumnInfoTable : Table("information_schema.columns") {
    val tableSchema = text("table_schema")
    val relationName = text("table_name")
    val columnName = text("column_name")
    val columnOrdinalPosition = integer("ordinal_position")
    val dataType = text("udt_name")
    val varcharLength = text("character_maximum_length")
    val isNullable = bool("is_nullable")
}
