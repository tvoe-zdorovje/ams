package by.anatolyloyko.ams.common.infrastructure.service.query

import org.assertj.core.api.WithAssertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(
    classes = [
        QueryGatewayImpl::class,
        QueryGatewayImplTest.TestQuery1QueryHandler::class,
        QueryGatewayImplTest.TestQuery2QueryHandler::class
    ]
)
class QueryGatewayImplTest : WithAssertions {
    @Autowired
    lateinit var gateway: QueryGatewayImpl

    @Test
    fun `must handle queries`() {
        val queryToHandlerMap = mapOf(
            TestQuery1() to TestQuery1QueryHandler(),
            TestQuery2() to TestQuery2QueryHandler()
        )

        val result = queryToHandlerMap.map { gateway.handle(it.key) }

        val expected = queryToHandlerMap.map { it.value.handle(it.key) }

        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `must fail when no handlers found`() {
        val query = TestQuery3()
        assertThatThrownBy { gateway.handle(query) }
            .isInstanceOf(IllegalStateException::class.java)
            .hasMessage("Cannot find query handler for $query")
            .rootCause()
            .isInstanceOf(NoSuchElementException::class.java)
            .hasMessage("Collection contains no element matching the predicate.")
    }

    @Test
    fun `must fail when multiple handlers found`() {
        val queryGateway = QueryGatewayImpl(
            queryHandlers = List(2) { TestQuery1QueryHandler() },
        )

        val query = TestQuery1()
        assertThatThrownBy { queryGateway.handle(query) }
            .isInstanceOf(IllegalStateException::class.java)
            .hasMessage("Cannot find query handler for $query")
            .rootCause()
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("Collection contains more than one matching element.")
    }

    class TestQuery1 : Query<String>

    class TestQuery2 : Query<String>

    class TestQuery3 : Query<String>

    class TestQuery1QueryHandler : BaseQueryHandler<TestQuery1, String>() {
        override fun handleInternal(query: TestQuery1): String =
            "${query.javaClass.simpleName} handled by ${this.javaClass.simpleName}"
    }

    class TestQuery2QueryHandler : BaseQueryHandler<TestQuery2, String>() {
        override fun handleInternal(query: TestQuery2): String =
            "${query.javaClass.simpleName} handled by ${this.javaClass.simpleName}"
    }
}
