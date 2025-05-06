package by.anatolyloyko.ams.common.infrastructure.service.query

import org.assertj.core.api.WithAssertions
import org.junit.jupiter.api.Test

class BaseQueryTest : WithAssertions {
    private val input = 123

    private val query = TestQuery(input)

    @Test
    fun `toString - must return correct string`() {
        assertThat(query.toString())
            .isEqualTo("${query.javaClass.simpleName}(input=$input)")
    }

    @Test
    fun `getInput - must return input`() {
        assertThat(query.input).isEqualTo(input)
    }

    private class TestQuery(input: Int) : BaseQuery<Int, String>(input)
}
