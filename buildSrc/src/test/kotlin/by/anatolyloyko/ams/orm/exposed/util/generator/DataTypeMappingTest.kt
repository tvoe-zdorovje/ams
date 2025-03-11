package by.anatolyloyko.ams.orm.exposed.util.generator

import by.anatolyloyko.ams.orm.exposed.util.generator.DataTypeMapping.Companion.findByColumnType
import org.assertj.core.api.WithAssertions
import org.junit.jupiter.api.Test

class DataTypeMappingTest : WithAssertions {
    @Test
    fun `must return lowercased name`() {
        val entry = DataTypeMapping.LONG

        assertThat(entry.lowercase()).isEqualTo(entry.name.lowercase())
    }

    @Test
    fun `must find entry by columnType`() {
        for (entry in DataTypeMapping.entries) {
            entry.columnTypes.forEach { columnType ->
                assertThat(findByColumnType(columnType)).isEqualTo(entry)
            }
        }
    }
}
