package by.anatolyloyko.ams.orm.util.generator

import by.anatolyloyko.ams.infrastructure.kotlin.alsoIf
import by.anatolyloyko.ams.infrastructure.kotlin.lowercaseFirstChar
import by.anatolyloyko.ams.orm.exposed.util.generator.DataTypeMapping
import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table
import java.io.File
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.reflect.KClass

private const val MODULE_DESTINATION_DIRECTORY = "src/main/kotlin"

private const val KDOC = """
 *  ██████████████████████████ DO NOT MODIFY! █████████████████████████
 *  █                                             
 *  █                    It's an autogenerated file.
 *  █     To regenerate, execute:
 *  █ ./gradlew :%L:generateDatabaseSchema
 *  █
 *  ███████████████████████████████████████████████████████████████████

 Represents a table in the database.
 
 Defines the structure of the table to facilitate database operations using the Exposed library.
"""

private const val VARCHAR_TYPE_NAME = "varchar"

private const val PRIMARY_KEY_PROPERTY_NAME = "primaryKey"

private const val ID_PROPERTY_NAME = "id"

private const val NULLABLE_INITIALIZER = "\n.nullable()"

private const val NAME_DELIMITER = "_"

/**
 * Abstract implementation of [SchemaGenerator] for generating Kotlin classes using the KotlinPoet library.
 *
 * The class relies on the KotlinPoet library
 * to generate the Kotlin code representing database tables based on schema information,
 * and it provides the structure to define the schema lookup logic via the abstract method [lookupSchemas].
 *
 * This implementation first looks up schemas by their names,
 * then generates Kotlin files based on the retrieved schema information.
 *
 * @param pathToDestinationModule the path to the module where the Kotlin class files will be generated.
 * @param destinationPackage the package name in which the Kotlin class files will be generated.
 */
internal abstract class KotlinPoetSchemaGenerator(
    private val pathToDestinationModule: String,
    private val destinationPackage: String,
) : SchemaGenerator {
    /**
     * Generates Kotlin classes representing the structure of tables in the provided schemas.
     *
     * This method looks up corresponding schema information for the given schema names,
     * and generates Kotlin classes for each schema. The method ensures that the list of schema names is not empty.
     *
     * @param schemaNames a list of schema names to generate Kotlin classes for.
     * @return a list of paths to the generated Kotlin class files.
     * @throws IllegalArgumentException if the `schemaNames` list is empty.
     *
     * @see lookupSchemas
     * @see generateSchema
     */
    override fun generate(schemaNames: Iterable<String>) =
        lookupSchemas(
            schemaNames
                .toList()
                .ifEmpty { throw IllegalArgumentException("Parameter 'schemaNames' must not be empty!") }
        )
            .flatMap(::generateSchema)

    /**
     * Abstract method for looking up schema information by their names.
     *
     * This method is responsible for finding the schema details for the provided list of schema names.
     * The implementation of this method will vary depending on how schema information is retrieved
     * (e.g., querying the database or reading from a configuration file).
     *
     * @param schemaNames a list of schema names to look up.
     * @return a list of [SchemaInfo] objects containing the details of the requested schemas.
     */
    protected abstract fun lookupSchemas(schemaNames: List<String>): List<SchemaInfo>

    /**
     * Generates Kotlin files based on the provided schema information using the KotlinPoet library.
     *
     * @param schemaInfo an object containing the schema details.
     * @return a list of paths to the generated Kotlin files.
     */
    private fun generateSchema(schemaInfo: SchemaInfo): List<Path> = schemaInfo
        .tables
        .map { tableInfo ->
            FileSpec
                .builder(
                    packageName = "$destinationPackage.${schemaInfo.name}",
                    fileName = tableInfo.name.normalize(true)
                )
                .addType(generateTypeSpec(tableInfo))
                .build()
        }
        .map { fileSpec ->
            val destinationDirectory = Paths.get(pathToDestinationModule, MODULE_DESTINATION_DIRECTORY)
            fileSpec.writeTo(destinationDirectory)

            Paths.get(destinationDirectory.toString(), fileSpec.relativePath)
        }

    private fun generateTypeSpec(tableInfo: SchemaInfo.TableInfo): TypeSpec {
        val className = tableInfo.name.normalize(true)

        return TypeSpec
            .objectBuilder(className)
            .addModifiers(KModifier.INTERNAL)
            .superclass(Table::class)
            .addKdoc()
            .suppress("MagicNumber")
            .addProperties(
                tableInfo
                    .columns
                    .map(::generatePropertySpec)
            )
            .addPrimarykey()
            .build()
    }

    private fun generatePropertySpec(columnInfo: SchemaInfo.TableInfo.ColumnInfo): PropertySpec {
        val name = columnInfo.name
        val type = columnInfo.type
        val isPrecisedVarchar = type == VARCHAR_TYPE_NAME && columnInfo.varcharLength != null

        return PropertySpec
            .builder(
                name = columnInfo.name.normalize(),
                type = Column::class.parameterizedBy(type.toKClass())
            )
            .initializer(
                CodeBlock
                    .builder()
                    .alsoIf(isPrecisedVarchar) { it.add("$type(%S, %L)", name, columnInfo.varcharLength) }
                    .alsoIf(!isPrecisedVarchar) { it.add("$type(%S)", name) }
                    .alsoIf(columnInfo.isNullable) { it.add(NULLABLE_INITIALIZER) }
                    .build()
            )
            .build()
    }

    private fun TypeSpec.Builder.addKdoc() = this.addKdoc(
        KDOC,
        pathToDestinationModule.substringAfterLast(File.separatorChar),
    )

    private fun TypeSpec.Builder.suppress(vararg values: String) = this.addAnnotation(
        AnnotationSpec
            .builder(Suppress::class)
            .addMember("%L", values.joinToString { "\"$it\"" })
            .build()
    )

    private fun TypeSpec.Builder.addPrimarykey() = this.addProperty(
        PropertySpec
            .builder(
                name = PRIMARY_KEY_PROPERTY_NAME,
                type = Table.PrimaryKey::class
            )
            .addModifiers(KModifier.OVERRIDE)
            .initializer("%L(%L)", Table.PrimaryKey::class.simpleName, ID_PROPERTY_NAME)
            .build()
    )

    private fun String.normalize(capitalizeFirstChar: Boolean = false): String = split(NAME_DELIMITER)
        .joinToString(separator = "") { part ->
            part.replaceFirstChar {
                if (it.isLowerCase()) {
                    it.titlecase()
                } else {
                    it.toString()
                }
            }
        }
        .alsoIf(!capitalizeFirstChar) { return it.lowercaseFirstChar() }

    private fun String.toKClass(): KClass<*> =
        DataTypeMapping
            .findByColumnType(this)
            .propertyType
}
