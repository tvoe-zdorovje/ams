package by.anatolyloyko.ams.orm.util.generator

import by.anatolyloyko.ams.orm.util.generator.exposed.ExposeSchemaGenerator
import by.anatolyloyko.ams.orm.util.generator.jooq.JooqSchemaGenerator
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

/**
 * Interface for generating Kotlin classes that represent database table structures.
 *
 * This interface defines the contract for generating Kotlin classes based on the provided schema names.
 * The generated classes represent the structure of tables within the specified database schemas.
 * These classes can be used to interact with the tables in the database through various database libraries.
 */
interface SchemaGenerator {
    /**
     * Generates Kotlin classes representing the structure of tables within the specified schemas.
     *
     * This method generates Kotlin files that define classes corresponding to tables in the specified schemas.
     * The generated classes provide properties that map to the columns of each table.
     *
     * @param schemaNames the list of schema names to generate Kotlin classes for.
     */
    fun generate(schemaNames: Iterable<String>)

    enum class Generators(
        private val kClass: KClass<out SchemaGenerator>,
    ) {
        EXPOSED(ExposeSchemaGenerator::class),
        JOOQ(JooqSchemaGenerator::class);

        /**
         * Builds an instance of [SchemaGenerator] using the specified database connection and file output parameters.
         *
         * @param url the database connection URL.
         * @param user the username for database authentication.
         * @param password the password for database authentication.
         * @param pathToDestinationModule the path to the module where the Kotlin class files will be generated.
         * @param group the group name in which the Kotlin class files will be generated.
         * @return an instance of [SchemaGenerator] that can be used to generate schema files.
         */
        fun build(
            url: String,
            user: String,
            password: String,
            pathToDestinationModule: String,
            group: String
        ) = kClass.primaryConstructor!!.call(url, user, password, pathToDestinationModule, group)
    }
}
