package by.anatolyloyko.ams.orm.exposed.util.generator

import org.assertj.core.api.WithAssertions
import org.junit.jupiter.api.Test

class DataTypeTest : WithAssertions {
    @Test
    fun `must find entry by columnType`() {
        for (entry in DataType.entries) {
            entry.columnTypes.forEach { columnType ->
                assertThat(DataType.findByColumnType(columnType)).isEqualTo(entry)
            }
        }
    }
}
