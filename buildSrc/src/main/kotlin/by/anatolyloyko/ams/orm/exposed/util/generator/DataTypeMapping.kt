package by.anatolyloyko.ams.orm.exposed.util.generator

import kotlin.reflect.KClass

/**
 * Enum class for mapping database column types to Kotlin property types.
 *
 * Each enum entry represents a mapping between a database column type and a corresponding Kotlin type.
 * This mapping is used to facilitate interaction with the Exposed library's table column definitions.
 *
 * The [columnTypes] represents the database column types (e.g., "int2", "int4", "varchar"),
 * while [propertyType] represents the corresponding Kotlin type (e.g., [Short::class], [Int::class], [String::class]).
 * The [name] of the enum entry corresponds to the canonical name used by the Exposed library to define column types.
 */
internal enum class DataTypeMapping(
    val columnTypes: Array<String>,
    val propertyType: KClass<*>
) {
    SMALLINT(
        arrayOf("smallint", "smallserial", "int2"),
        Short::class
    ),
    INTEGER(
        arrayOf("integer", "int", "int4", "serial"),
        Int::class
    ),
    LONG(
        arrayOf("long", "bigint", "int8"),
        Long::class
    ),
    VARCHAR(
        arrayOf("varchar", "character varying", "text"),
        String::class
    ),

    ;

    /**
     * Converts the name of the enum entry to lowercase to match Exposed's column type naming convention.
     *
     * This method is used to return the name of the enum in lowercase, which is aligned with the
     * typical column type names used by the Exposed library (such as "integer", "varchar").
     *
     * @return the lowercase version of the enum's name.
     */
    fun lowercase(): String = name.lowercase()

    companion object {
        /**
         * Finds the corresponding [DataTypeMapping] by database column type.
         *
         * @param columnType the column type from the database.
         * @return the matching [DataTypeMapping] entry.
         * @throws NoSuchElementException if no matching column type is found.
         */
        fun findByColumnType(columnType: String): DataTypeMapping =
            entries
                .find { columnType in it.columnTypes }
                ?: throw NoSuchElementException()
    }
}
