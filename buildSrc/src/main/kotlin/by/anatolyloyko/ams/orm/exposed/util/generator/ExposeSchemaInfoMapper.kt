package by.anatolyloyko.ams.orm.exposed.util.generator

import by.anatolyloyko.ams.orm.util.generator.SchemaInfo
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
     * @param data the collection of [ResultRow] containing metadata about database columns.
     * @return a list of [SchemaInfo] objects representing the table schemas.
     */
    fun map(data: Collection<ResultRow>): List<SchemaInfo> =
        data
            .groupBy { it[TableColumnInfoTable.tableSchema] }
            .map(ExposeSchemaInfoMapper::mapSchema)

    private fun mapSchema(schemaInfo: Map.Entry<String, List<ResultRow>>): SchemaInfo =
        SchemaInfo(
            name = schemaInfo.key,
            tables = schemaInfo
                .value
                .groupBy { it[TableColumnInfoTable.relationName] }
                .map(ExposeSchemaInfoMapper::mapTable)
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
            type = mapDataType(columnInfo[TableColumnInfoTable.dataType]),
            varcharLength = columnInfo
                .getOrNull(TableColumnInfoTable.varcharLength)
                ?.toInt(),
            isNullable = columnInfo[TableColumnInfoTable.isNullable],
        )

    private fun mapDataType(dataType: String): String = DataTypeMapping.findByColumnType(dataType).lowercase()
}
