package by.anatolyloyko.ams.orm.exposed.util.generator

import by.anatolyloyko.ams.orm.util.generator.KotlinPoetSchemaGenerator
import by.anatolyloyko.ams.orm.util.generator.SchemaInfo
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

/**
 * Concrete implementation of the [KotlinPoetSchemaGenerator] that generates Kotlin classes
 * based on schema information retrieved via the Exposed library.
 *
 * This implementation is responsible for querying the database schema information for the provided
 * schema names.
 *
 * It connects to the database using the provided credentials and URL, and uses the Exposed
 * library to retrieve metadata about the tables in the specified schemas.
 *
 * @param url the database connection URL.
 * @param user the username for database authentication.
 * @param password the password for database authentication.
 * @param pathToDestinationModule the path to the module where the Kotlin class files will be generated.
 * @param destinationPackage the package name in which the Kotlin class files will be generated.
 */
internal class ExposeSchemaGenerator(
    private val url: String,
    private val user: String,
    private val password: String,
    pathToDestinationModule: String,
    destinationPackage: String,
) : KotlinPoetSchemaGenerator(
    pathToDestinationModule = pathToDestinationModule,
    destinationPackage = destinationPackage
) {
    /**
     * Looks up schema information for the provided list of schema names using the Exposed library.
     *
     * This method queries the `information_schema.columns` table to retrieve column details
     * for tables within the provided schemas.
     *
     * @param schemaNames a list of schema names to fetch metadata for.
     * @return a list of [SchemaInfo] containing the details for each schema.
     * @see TableColumnInfoTable
     */
    override fun lookupSchemas(schemaNames: List<String>): List<SchemaInfo> = transaction(
        Database.connect(
            url = url,
            user = user,
            password = password
        )
    ) {
        TableColumnInfoTable
            .selectAll()
            .where { TableColumnInfoTable.tableSchema inList schemaNames }
            .orderBy(
                TableColumnInfoTable.relationName to SortOrder.ASC,
                TableColumnInfoTable.columnOrdinalPosition to SortOrder.ASC
            )
            .toList()
            .let(ExposeSchemaInfoMapper::map)
    }
}
