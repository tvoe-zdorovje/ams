package by.anatolyloyko.ams.auth.kafka

import by.anatolyloyko.ams.auth.IDP_USER_ID
import by.anatolyloyko.ams.auth.USER
import by.anatolyloyko.ams.auth.token.action.PublishTokenAction
import by.anatolyloyko.ams.auth.token.command.GenerateTokenCommand
import by.anatolyloyko.ams.auth.token.command.TokenCommandHandler
import by.anatolyloyko.ams.auth.token.command.input.GenerateTokenCommandInput
import by.anatolyloyko.ams.auth.user.finder.UserFinder
import com.fasterxml.jackson.databind.ObjectMapper
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifyOrder
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.junit.jupiter.api.Test
import org.springframework.kafka.support.Acknowledgment
import java.time.Duration

private const val TOKEN = "access-token"

private val GENERATE_TOKEN_COMMAND = GenerateTokenCommand(GenerateTokenCommandInput(USER))

private val EVENT_TYPES = listOf("CODE_TO_TOKEN", "REFRESH_TOKEN")

class KeycloakEventsKafkaListenerTest {
    private val userFinder = mockk<UserFinder> {
        every { byIdpUUID(IDP_USER_ID) } returns USER
    }

    private val tokenCommandHandler = mockk<TokenCommandHandler> {
        every { handle(GENERATE_TOKEN_COMMAND) } returns TOKEN
    }

    private val publishTokenAction = mockk<PublishTokenAction>(relaxed = true)

    private val acknowledgment = mockk<Acknowledgment>(relaxed = true)

    private val listener = KeycloakEventsKafkaListener(
        objectMapper = ObjectMapper(),
        userFinder = userFinder,
        tokenCommandHandler = tokenCommandHandler,
        publishTokenAction = publishTokenAction,
        eventTypes = EVENT_TYPES
    )

    @Test
    fun `must generate and publish token and acknowledge code-to-token events`() {
        listener.onKeycloakEvent(record(CODE_TO_TOKEN_KEYCLOAK_EVENT_PAYLOAD), acknowledgment)

        verifyOrder {
            userFinder.byIdpUUID(IDP_USER_ID)
            tokenCommandHandler.handle(GENERATE_TOKEN_COMMAND)
            publishTokenAction(IDP_USER_ID, TOKEN)
            acknowledgment.acknowledge()
        }
        verify(exactly = 0) { acknowledgment.nack(any<Duration>()) }
    }

    @Test
    fun `must generate and publish token and acknowledge refresh-token events`() {
        listener.onKeycloakEvent(record(REFRESH_TOKEN_KEYCLOAK_EVENT_PAYLOAD), acknowledgment)

        verifyOrder {
            userFinder.byIdpUUID(IDP_USER_ID)
            tokenCommandHandler.handle(GENERATE_TOKEN_COMMAND)
            publishTokenAction(IDP_USER_ID, TOKEN)
            acknowledgment.acknowledge()
        }
        verify(exactly = 0) { acknowledgment.nack(any<Duration>()) }
    }

    @Test
    fun `must ignore unknown event and acknowledge message`() {
        listener.onKeycloakEvent(record("""{"type":"UNKNOWN"}"""), acknowledgment)

        verify(exactly = 1) { acknowledgment.acknowledge() }
        verify(exactly = 0) { userFinder.byIdpUUID(any()) }
        verify(exactly = 0) { tokenCommandHandler.handle(any()) }
        verify(exactly = 0) { publishTokenAction(any(), any()) }
        verify(exactly = 0) { acknowledgment.nack(any<Duration>()) }
    }

    @Test
    fun `must nack invalid json`() {
        listener.onKeycloakEvent(record("not-json"), acknowledgment)

        verify(exactly = 0) { acknowledgment.acknowledge() }
        verify(exactly = 0) { userFinder.byIdpUUID(any()) }
        verify(exactly = 0) { tokenCommandHandler.handle(any()) }
        verify(exactly = 0) { publishTokenAction(any(), any()) }
        verify(exactly = 1) { acknowledgment.nack(any<Duration>()) }
    }

    @Test
    fun `must nack when no user found`() {
        val unknownExternalUserId = "96738aa3-a681-4620-8006-8d07e3774239"
        every { userFinder.byIdpUUID(unknownExternalUserId) } returns null

        val payload = REFRESH_TOKEN_KEYCLOAK_EVENT_PAYLOAD.replace(IDP_USER_ID, unknownExternalUserId)
        listener.onKeycloakEvent(record(payload), acknowledgment)

        verify(exactly = 1) { userFinder.byIdpUUID(unknownExternalUserId) }
        verify(exactly = 0) { acknowledgment.acknowledge() }
        verify(exactly = 0) { tokenCommandHandler.handle(any()) }
        verify(exactly = 0) { publishTokenAction(any(), any()) }
        verify(exactly = 1) { acknowledgment.nack(any<Duration>()) }
    }

    private fun record(payload: String) = ConsumerRecord(
        "v1.keycloak.events",
        0,
        0,
        "key-1",
        payload,
    )
}
