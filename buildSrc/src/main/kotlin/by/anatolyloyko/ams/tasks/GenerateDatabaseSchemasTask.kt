package by.anatolyloyko.ams.tasks

import by.anatolyloyko.ams.orm.util.generator.SchemaGenerator
import org.gradle.api.Project
import org.yaml.snakeyaml.Yaml

private const val SOURCE_DIRECTORY = "src/main/resources"

private const val CONFIG_FILE_NAME = "application.yaml"

/**
 * Task that generates Kotlin files representing database schemas.
 *
 * This class is responsible for invoking the schema generation process
 * using the necessary database connection information from the project's datasource configuration
 * and the provided schema names.
 *
 * @param project the Gradle project object.
 */
class GenerateDatabaseSchemasTask(
    private val project: Project
) {
    /**
     * Executes the schema generation task for the specified schemas.
     *
     * @param schemas the names of the schemas for which Kotlin representation classes will be generated.
     * @throws IllegalArgumentException if no schemas are provided.
     * @see SchemaGenerator
     */
    fun execute(vararg schemas: String) {
        val moduleName = project.name
        val schemaNames = schemas
            .toList()
            .ifEmpty { null }
            ?: error("No value passed to parameter 'schemas'")

        println("""
            ================================================================================================
                Generating database schemas '$schemaNames' for module '$moduleName'
            ================================================================================================
        """)

        val datasourceConfig = findDatasourceConfig()

        val dbUrl = datasourceConfig["url"] ?: error("The database URL not found")
        val dbUsername = datasourceConfig["username"] ?: error("The database username not found")
        val dbPassword = datasourceConfig["password"] ?: error("The database password not found")

        println("Using database URL: $dbUrl")

        SchemaGenerator
            .buildGenerator(
                url = dbUrl,
                user = dbUsername,
                password = dbPassword,
                pathToDestinationModule = project.projectDir.absolutePath,
                destinationPackage = "${project.group}.orm.exposed.schemas"
            )
            .generate(schemaNames)
            .forEach { println("\nGenerated: $it") }
    }

    private fun findDatasourceConfig(): Map<String, String> {
        val springConfigFile = project
            .layout
            .projectDirectory
            .dir(SOURCE_DIRECTORY)
            .file(CONFIG_FILE_NAME)
            .asFile
        if (!springConfigFile.exists()) {
            error("The configuration file '$CONFIG_FILE_NAME' does not exist")
        }

        val springConfig = springConfigFile.reader(Charsets.UTF_8).use { Yaml().load<Map<String, Any>>(it) }

        val datasourceConfig = (springConfig["spring"] as? Map<String, Map<String, String>>)
            ?.get("datasource")
            ?: error("The datasource configuration not found")

        return datasourceConfig
    }
}
