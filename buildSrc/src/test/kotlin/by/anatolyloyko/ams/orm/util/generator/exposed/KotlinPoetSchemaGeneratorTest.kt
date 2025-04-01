package by.anatolyloyko.ams.orm.util.generator.exposed

import com.squareup.kotlinpoet.FileSpec
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verifyOrder
import org.assertj.core.api.WithAssertions
import org.junit.jupiter.api.Test
import java.nio.file.Path

private const val NUMBER_OF_OBJECTS = 3

class KotlinPoetSchemaGeneratorTest : WithAssertions {
    private val schemaInfo = SchemaInfo(
        name = "schema_name",
        tables = List(NUMBER_OF_OBJECTS) { mockk() },
        functions = List(NUMBER_OF_OBJECTS) { mockk() }
    )

    private val schemaGenerator = object : KotlinPoetSchemaGenerator(
        pathToDestinationModule = "",
        destinationPackage = ""
    ) {
        override fun lookupSchemas(schemaNames: List<String>): List<SchemaInfo> = listOf(
            schemaInfo
        )
    }

    @Test
    fun `must throw exception when schemaNames list is empty`() {
        assertThatThrownBy { schemaGenerator.generate(emptyList()) }
            .hasMessage("Parameter 'schemaNames' must not be empty!")
    }

    @Test
    fun `must lookup schemas and generate classes for each schema`() {
        val pathMockk = mockk<Path>()
        val fileSpecMockk = mockk<FileSpec> {
            every { writeTo(any<Path>()) } returns pathMockk
            every { relativePath } returns ""
        }
        val spy = spyk(schemaGenerator, recordPrivateCalls = true)
        every { spy["buildTableFileSpec"](any<String>(), any<SchemaInfo.TableInfo>()) } returns fileSpecMockk
        every { spy["buildFunctionFileSpec"](any<String>(), any<SchemaInfo.FunctionInfo>()) } returns fileSpecMockk

        spy.generate(listOf("schema"))

        verifyOrder {
            spy["lookupSchemas"](any<List<String>>())
            spy["generateSchema"](schemaInfo)


            repeat(NUMBER_OF_OBJECTS) {
                spy["buildTableFileSpec"](any<String>(), any<SchemaInfo.TableInfo>())
            }
            repeat(NUMBER_OF_OBJECTS) {
                spy["buildFunctionFileSpec"](any<String>(), any<SchemaInfo.FunctionInfo>())
            }
        }
    }
}