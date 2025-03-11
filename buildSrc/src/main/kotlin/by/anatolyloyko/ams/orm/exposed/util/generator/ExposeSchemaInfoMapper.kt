package by.anatolyloyko.ams.orm.exposed.util.generator

import org.jetbrains.exposed.sql.ResultRow

/**
 * Responsible for mapping [ResultRow] data to a list of [SchemaInfo] objects.
 *
 * This object groups the input data by schema
 * and transforms it into a structured representation for further processing.
 */
// todo implement unit tests
internal object ExposeSchemaInfoMapper {
    /**
     * Maps a collection of Exposed `ResultRow` objects to a list of [SchemaInfo] objects.
     *
     * @param tablesData the collection of [ResultRow] containing metadata about table.
     * @return a list of [SchemaInfo] objects representing the schemas.
     */
    fun map(
        tablesData: Collection<ResultRow>,
        functionsData: Collection<ResultRow>
    ): List<SchemaInfo> {
        val schemaToTablesData = tablesData.groupBy { it[TableColumnInfoTable.tableSchema] }
        val schemaToFunctionsData = functionsData.groupBy { it[PgNamespaceTable.name] }

        return (schemaToTablesData.keys + schemaToFunctionsData.keys)
            .distinct()
            .map { schemaName ->
                mapSchema(
                    schemaName,
                    schemaToTablesData[schemaName] ?: emptyList(),
                    schemaToFunctionsData[schemaName] ?: emptyList()
                )
            }
    }

    private fun mapSchema(
        schemaName: String,
        tablesData: List<ResultRow>,
        functionsData: Collection<ResultRow>
    ): SchemaInfo = SchemaInfo(
        name = schemaName,
        tables = tablesData
            .groupBy { it[TableColumnInfoTable.relationName] }
            .map(ExposeSchemaInfoMapper::mapTable),
        functions = functionsData
            .map(ExposeSchemaInfoMapper::mapFunction)
    )

    private fun mapTable(tableInfo: Map.Entry<String, List<ResultRow>>): SchemaInfo.TableInfo =
        SchemaInfo.TableInfo(
            name = tableInfo.key,
            columns = tableInfo
                .value
                .sortedBy { it[TableColumnInfoTable.columnOrdinalPosition] }
                .map(ExposeSchemaInfoMapper::mapColumn)
        )

    private fun mapColumn(columnInfo: ResultRow): SchemaInfo.TableInfo.ColumnInfo =
        SchemaInfo.TableInfo.ColumnInfo(
            name = columnInfo[TableColumnInfoTable.columnName],
            type = DataType.findByColumnType(columnInfo[TableColumnInfoTable.dataType]),
            varcharLength = columnInfo
                .getOrNull(TableColumnInfoTable.varcharLength)
                ?.toInt(),
            isNullable = columnInfo[TableColumnInfoTable.isNullable],
        )

    private fun mapFunction(functionInfo: ResultRow): SchemaInfo.FunctionInfo = SchemaInfo.FunctionInfo(
        name = functionInfo[PgProcTable.name],
        type = when (functionInfo[PgProcTable.type]) {
            "f" -> "function"
            "p" -> "procedure"
            else -> throw IllegalArgumentException("Unknown function type: ${functionInfo[PgProcTable.type]}")
        },
        resultType = DataType.findByColumnType(functionInfo[PgProcTable.resultType]!!),
        arguments = mapArguments(functionInfo[PgProcTable.arguments]!!)
    )

    private fun mapArguments(arguments: String): List<SchemaInfo.FunctionInfo.ArgumentInfo> = arguments
        .split(", ")
        .map { nameToTypeString ->
            SchemaInfo.FunctionInfo.ArgumentInfo(
                name = nameToTypeString.substringBefore(' '),
                type = DataType.findByColumnType(nameToTypeString.substringAfter(' '))
            )
        }
}
