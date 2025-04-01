package by.anatolyloyko.ams.orm.util.generator.exposed

internal data class SchemaInfo(
    val name: String,
    val tables: List<TableInfo>,
    val functions: List<FunctionInfo>
) {
    data class TableInfo(
        val name: String,
        val columns: List<ColumnInfo>
    ) {
        data class ColumnInfo(
            val name: String,
            val type: DataType,
            val varcharLength: Int?,
            val isNullable: Boolean
        )
    }

    data class FunctionInfo(
        val name: String,
        val type: String,
        val resultType: DataType,
        val arguments: List<ArgumentInfo>
    ) {
        data class ArgumentInfo(
            val name: String,
            val type: DataType,
        )
    }
}
