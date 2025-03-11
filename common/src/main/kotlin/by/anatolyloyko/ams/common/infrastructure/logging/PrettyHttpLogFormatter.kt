package by.anatolyloyko.ams.common.infrastructure.logging

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import org.springframework.stereotype.Component
import org.zalando.logbook.Correlation
import org.zalando.logbook.HttpMessage
import org.zalando.logbook.HttpRequest
import org.zalando.logbook.HttpResponse
import org.zalando.logbook.Origin
import org.zalando.logbook.Precorrelation
import org.zalando.logbook.StructuredHttpLogFormatter
import java.util.Optional

/**
 * A custom implementation of [StructuredHttpLogFormatter] that formats HTTP logs in a human-readable,
 * pretty-printed JSON structure.
 */
@Component
class PrettyHttpLogFormatter : StructuredHttpLogFormatter {
    private val objectMapper = ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT)

    /**
     * Formats an HTTP request before correlation is established.
     *
     * @param precorrelation The precorrelation ID associated with the request.
     * @param request The HTTP request being logged.
     * @return A formatted string representation of the HTTP request.
     */
    override fun format(precorrelation: Precorrelation, request: HttpRequest): String {
        val preparedContent = prepare(precorrelation, request)
        val delegateFormat = format(preparedContent)

        return """
            |${request.direction()} Request: ${request.method} ${request.requestUri}
            |$delegateFormat
        """.trimMargin()
    }

    /**
     * Formats an HTTP response after correlation is established.
     *
     * @param correlation The correlation ID associated with the response.
     * @param response The HTTP response being logged.
     * @return A formatted string representation of the HTTP response.
     */
    override fun format(correlation: Correlation, response: HttpResponse): String {
        val content = prepare(correlation, response)
        val delegateFormat = format(content)

        val duration = correlation.duration.toMillis()
        return """
            |${response.direction()} Response: ${response.status} ${response.reasonPhrase} [$duration ms]
            |$delegateFormat
        """.trimMargin()
    }

    /**
     * Parses the request/response body into a structured JSON format, if applicable.
     *
     * @param message The HTTP message containing the body.
     * @return An optional containing the parsed JSON body as a [Map], or empty if the body is empty.
     * @see StructuredHttpLogFormatter.prepareBody
     */
    override fun prepareBody(message: HttpMessage): Optional<Any> = super.prepareBody(message)
        .map { objectMapper.readValue(it as String, Map::class.java) }

    /**
     * Serializes the given structured content into a formatted JSON string.
     *
     * @param content The structured HTTP log content.
     * @return A pretty-printed JSON representation of the content.
     */
    override fun format(content: MutableMap<String, Any>): String = objectMapper.writeValueAsString(content)

    /**
     * Determines whether the HTTP message is an incoming or outgoing request.
     *
     * @return `"Incoming"` if the message originated remotely, `"Outgoing"` otherwise.
     */
    private fun HttpMessage.direction(): String = if (origin == Origin.REMOTE) "Incoming" else "Outgoing"
}
