package by.anatolyloyko.ams.orm.util.generator

internal data class SchemaInfo(
    val name: String,
    val tables: List<TableInfo>
) {
    data class TableInfo(
        val name: String,
        val columns: List<ColumnInfo>
    ) {
        data class ColumnInfo(
            val name: String,
            val type: String,
            val varcharLength: Int?,
            val isNullable: Boolean
        )
    }
}
