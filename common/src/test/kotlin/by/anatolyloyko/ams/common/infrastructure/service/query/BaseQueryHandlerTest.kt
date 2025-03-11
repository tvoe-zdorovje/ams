package by.anatolyloyko.ams.common.infrastructure.service.query

import org.assertj.core.api.WithAssertions
import org.junit.jupiter.api.Test

class BaseQueryHandlerTest : WithAssertions {
    private val queryHandler = object : BaseQueryHandler<TestQuery, Unit>() {
        override fun handleInternal(query: TestQuery) = TODO("Not yet implemented")
    }

    @Test
    fun `canHandle - must return true`() {
        assertThat(queryHandler.canHandle(TestQuery()))
    }

    @Test
    fun `canHandle - must return false`() {
        val query = object : Query<Unit> {}

        assertThat(queryHandler.canHandle(query)).isFalse()
    }

    class TestQuery : Query<Unit>
}
