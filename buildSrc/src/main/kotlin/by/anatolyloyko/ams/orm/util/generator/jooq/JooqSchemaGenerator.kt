package by.anatolyloyko.ams.orm.util.generator.jooq

import by.anatolyloyko.ams.orm.util.generator.SchemaGenerator
import org.jooq.codegen.GenerationTool
import org.jooq.codegen.KotlinGenerator
import org.jooq.meta.jaxb.Configuration
import org.jooq.meta.jaxb.Database
import org.jooq.meta.jaxb.Generate
import org.jooq.meta.jaxb.Generator
import org.jooq.meta.jaxb.SchemaMappingType
import org.jooq.meta.jaxb.Target
import org.jooq.meta.postgres.PostgresDatabase
import java.sql.DriverManager

// This is a Java regular expression.
// Use the pipe to separate several expressions.
private const val EXCLUDES: String = "next_id"

/**
 * JOOQ implementation of the [SchemaGenerator] that generates Kotlin classes based on database schema information.
 *
 * It connects to the database using the provided credentials and URL, and uses the JOOQ library to retrieve metadata
 * about the tables and routines in the specified schemas.
 *
 * @param url the database connection URL.
 * @param user the username for database authentication.
 * @param password the password for database authentication.
 * @param pathToDestinationModule the path to the module where the Kotlin class files will be generated.
 * @param group the group name in which the Kotlin class files will be generated.
 */
internal class JooqSchemaGenerator(
    private val url: String,
    private val user: String,
    private val password: String,
    pathToDestinationModule: String,
    group: String,
) : SchemaGenerator {
    private val destinationDirectory = "$pathToDestinationModule/src/main/kotlin"
    private val destinationPackage = "$group.orm.jooq.schemas"

    override fun generate(schemaNames: Iterable<String>) = DriverManager.getConnection(
        url,
        user,
        password
    ).use { connection ->
        GenerationTool().run {
            setConnection(connection)
            val configuration = configuration()
            schemaNames.forEach { schemaName ->
                configuration.generator.apply {
                    val schema = SchemaMappingType().withInputSchema(schemaName)
                    database.schemata = listOf(schema)
                    target.packageName = "${target.packageName}.$schemaName"
                }

                run(configuration)
            }
        }
    }

    private fun configuration(): Configuration = Configuration().withGenerator(
        Generator().apply {
            this.name = KotlinGenerator::class.qualifiedName
            this.database = Database()
                .apply {
                    name = PostgresDatabase::class.qualifiedName

                    excludes = EXCLUDES
                }
            this.generate = Generate().apply {
                isDefaultCatalog = false
                isKeys = false
                isSequences = false
                isKotlinNotNullRecordAttributes = true
                isRenameMethodOverrides = false
                isWhereMethodOverrides = false
                isAsMethodOverrides = false
                isRecordsImplementingRecordN = true
                // isPojos = true
            }
            this.target = Target()
                .apply {
                    packageName = destinationPackage
                    directory = destinationDirectory

                    isClean = false
                }
        }
    )
}
