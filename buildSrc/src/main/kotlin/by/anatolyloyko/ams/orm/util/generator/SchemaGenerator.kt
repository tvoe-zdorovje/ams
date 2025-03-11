package by.anatolyloyko.ams.orm.util.generator

import by.anatolyloyko.ams.orm.exposed.util.generator.ExposeSchemaGenerator
import java.nio.file.Path

/**
 * Interface for generating Kotlin classes that represent database table structures.
 *
 * This interface defines the contract for generating Kotlin classes based on the provided schema names.
 * The generated classes represent the structure of tables within the specified database schemas.
 * These classes can be used to interact with the tables in the database through various database libraries.
 */
internal interface SchemaGenerator {
    /**
     * Generates Kotlin classes representing the structure of tables within the specified schemas.
     *
     * This method generates Kotlin files that define classes corresponding to tables in the specified schemas.
     * The generated classes provide properties that map to the columns of each table.
     *
     * @param schemaNames the list of schema names to generate Kotlin classes for.
     * @return a list of paths to the generated Kotlin class files.
     */
    fun generate(schemaNames: Iterable<String>): List<Path>

    companion object {
        /**
         * Builds an instance of [SchemaGenerator] using the specified database connection and file output parameters.
         *
         * @param url the database connection URL.
         * @param user the username for database authentication.
         * @param password the password for database authentication.
         * @param pathToDestinationModule the path to the module where the Kotlin class files will be generated.
         * @param destinationPackage the package name in which the Kotlin class files will be generated.
         * @return an instance of [SchemaGenerator] that can be used to generate schema files.
         */
        fun buildGenerator(
            url: String,
            user: String,
            password: String,
            pathToDestinationModule: String,
            destinationPackage: String
        ): SchemaGenerator = ExposeSchemaGenerator(
            url = url,
            user = user,
            password = password,
            pathToDestinationModule = pathToDestinationModule,
            destinationPackage = destinationPackage,
        )
    }
}
