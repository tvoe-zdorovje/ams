package by.anatolyloyko.ams.common.infrastructure.logging

import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import org.assertj.core.api.WithAssertions
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import org.zalando.logbook.Correlation
import org.zalando.logbook.HttpMessage
import org.zalando.logbook.HttpRequest
import org.zalando.logbook.HttpResponse
import org.zalando.logbook.Origin
import org.zalando.logbook.Precorrelation
import java.time.Duration

private const val METHOD = "GET"

private const val URI = "/restapi/resource"

private const val CONTENT = "{content}"

private val PREPARED = mutableMapOf<String, Any>()

private val STATUS = HttpStatus.OK.value()

private const val REASON_PHRASE = "reasonPhrase"

class PrettyHttpLogFormatterTest : WithAssertions {
    private val formatter = PrettyHttpLogFormatter()

    @Test
    fun `format - must format request`() {
        val precorrelation = mockk<Precorrelation>()
        val request = mockk<HttpRequest> {
            every { origin } returns Origin.REMOTE
            every { method } returns METHOD
            every { requestUri } returns URI
        }
        val formatterSpy = spyk(formatter) {
            every { format(any()) } returns CONTENT
            every { prepare(precorrelation, request) } returns PREPARED
        }

        val result = formatterSpy.format(precorrelation, request)

        val expectedResult =  """
            |Incoming Request: $METHOD $URI
            |$CONTENT
        """.trimMargin()

        assertThat(result).isEqualTo(expectedResult)
        verify(exactly = 1) {
            formatterSpy.prepare(precorrelation, request)
            formatterSpy.format(PREPARED)
        }
    }

    @Test
    fun `format - must format response`() {
        val corelation = mockk<Correlation> {
            every { duration } returns Duration.ZERO
        }

        val response = mockk<HttpResponse> {
            every { origin } returns Origin.LOCAL
            every { status } returns STATUS
            every { reasonPhrase } returns REASON_PHRASE
        }
        val formatterSpy = spyk(formatter) {
            every { format(any()) } returns CONTENT
            every { prepare(corelation, response) } returns PREPARED
        }

        val result = formatterSpy.format(corelation, response)

        val expectedResult =  """
            |Outgoing Response: $STATUS $REASON_PHRASE [0 ms]
            |$CONTENT
        """.trimMargin()

        assertThat(result).isEqualTo(expectedResult)
        verify(exactly = 1) {
            formatterSpy.prepare(corelation, response)
            formatterSpy.format(PREPARED)
        }
    }

    @Test
    fun `format - must format content`() {
        val content: MutableMap<String, Any> = mutableMapOf(
            "param1" to "value1",
            "param2" to "value2"
        )
        val expectedResult =  """{
            |  "param1" : "value1",
            |  "param2" : "value2"
        |}""".trimMargin()

        val result = formatter.format(content)

        assertThat(result).isEqualTo(expectedResult)
    }

    @Test
    fun `prepareBody - must parse message to map`() {
        val expectedResult = mapOf("parameter" to "value")
        val message = "{\"parameter\": \"value\"}"
        val httpMessage = mockk<HttpMessage>() {
            every { bodyAsString } returns message
        }

        val result = formatter.prepareBody(httpMessage)

        assertThat(result.get()).isEqualTo(expectedResult)
    }
}
