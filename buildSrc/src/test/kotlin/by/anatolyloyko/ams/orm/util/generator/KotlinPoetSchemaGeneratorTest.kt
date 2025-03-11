package by.anatolyloyko.ams.orm.util.generator

import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verifyOrder
import org.assertj.core.api.WithAssertions
import org.junit.jupiter.api.Test
import java.nio.file.Path

private const val NUMBER_OF_SCHEMAS = 3

class KotlinPoetSchemaGeneratorTest : WithAssertions {
    private val schemaGenerator = object : KotlinPoetSchemaGenerator(
        pathToDestinationModule = "",
        destinationPackage = ""
    ) {
        override fun lookupSchemas(schemaNames: List<String>): List<SchemaInfo> = List(NUMBER_OF_SCHEMAS) { mockk() }
    }

    @Test
    fun `must throw exception when schemaNames list is empty`() {
        assertThatThrownBy { schemaGenerator.generate(emptyList()) }
            .hasMessage("Parameter 'schemaNames' must not be empty!")
    }

    @Test
    fun `must lookup schemas and generate classes for each schema`() {
        val spy = spyk(schemaGenerator, recordPrivateCalls = true)
        every { spy["generateSchema"](any<SchemaInfo>()) } returns listOf<Path>()

        spy.generate(listOf("schema"))

        verifyOrder {
            spy["lookupSchemas"](any<List<String>>())
            for (i in 0 until NUMBER_OF_SCHEMAS) {
                spy["generateSchema"](any<SchemaInfo>())
            }
        }
    }
}
