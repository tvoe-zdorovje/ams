package by.anatolyloyko.ams.orm.util.generator.exposed

import org.jetbrains.exposed.sql.ColumnType
import org.jetbrains.exposed.sql.IntegerColumnType
import org.jetbrains.exposed.sql.LongColumnType
import org.jetbrains.exposed.sql.ShortColumnType
import org.jetbrains.exposed.sql.TextColumnType
import org.jetbrains.exposed.sql.VarCharColumnType
import kotlin.reflect.KClass

/**
 * Enum class for mapping database column types to Kotlin property types.
 *
 * Each enum entry represents a mapping between a database column type and a corresponding Kotlin type.
 * This mapping is used to facilitate interaction with the Exposed library's table column definitions.
 *
 * [columnTypes] represents the database column types (e.g., "int2", "int4", "varchar"),
 * [propertyType] is a corresponding Kotlin type (e.g., [Short::class], [Int::class], [String::class]),
 * [exposedColumnType] is a corresponding column type from Exposed Library (e.g., [ShortColumnType::class])
 * The [name] of the enum entry corresponds to the canonical name used by the Exposed library to define column types.
 */
@Suppress("EnumNaming")
internal enum class DataType(
    val columnTypes: Array<String>,
    val propertyType: KClass<*>,
    val exposedColumnType: ColumnType<*>
) {
    smallint(
        arrayOf("smallint", "smallserial", "int2"),
        Short::class,
        ShortColumnType()
    ),
    integer(
        arrayOf("integer", "int", "int4", "serial"),
        Int::class,
        IntegerColumnType()
    ),
    long(
        arrayOf("long", "bigint", "int8"),
        Long::class,
        LongColumnType()
    ),
    varchar(
        arrayOf("varchar", "character varying", "text"),
        String::class,
        VarCharColumnType()
    ),
    text(
        arrayOf("regclass"),
        String::class,
        TextColumnType()
    ),

    ;

    companion object {
        /**
         * Finds the corresponding [DataType] by database column type.
         *
         * @param columnType the column type from the database.
         * @return the matching [DataType] entry.
         * @throws NoSuchElementException if no matching column type is found.
         */
        fun findByColumnType(columnType: String): DataType =
            entries
                .find { columnType in it.columnTypes }
                ?: throw NoSuchElementException("Data Type for '$columnType' column type not found")
    }
}