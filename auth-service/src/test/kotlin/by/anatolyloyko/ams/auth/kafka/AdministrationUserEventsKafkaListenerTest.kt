package by.anatolyloyko.ams.auth.kafka

import by.anatolyloyko.ams.auth.IDP_USER_ID
import by.anatolyloyko.ams.auth.USER
import by.anatolyloyko.ams.auth.USER_ID
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
import org.assertj.core.api.WithAssertions
import org.junit.jupiter.api.Test
import org.springframework.kafka.support.Acknowledgment
import java.time.Duration

private const val TOKEN = "access-token"

private val GENERATE_TOKEN_COMMAND = GenerateTokenCommand(GenerateTokenCommandInput(USER))

private val EVENT_TYPES = listOf("UPDATE_ROLE")

class AdministrationUserEventsKafkaListenerTest : WithAssertions {
    private val userFinder = mockk<UserFinder> {
        every { byId(USER_ID) } returns USER
    }

    private val tokenCommandHandler = mockk<TokenCommandHandler> {
        every { handle(GENERATE_TOKEN_COMMAND) } returns TOKEN
    }

    private val publishTokenAction = mockk<PublishTokenAction>(relaxed = true)

    private val acknowledgment = mockk<Acknowledgment>(relaxed = true)

    private val listener = AdministrationUserEventsKafkaListener(
        objectMapper = ObjectMapper(),
        userFinder = userFinder,
        tokenCommandHandler = tokenCommandHandler,
        publishTokenAction = publishTokenAction,
        eventTypes = EVENT_TYPES
    )

    @Test
    fun `must generate and publish token and acknowledge update-role events`() {
        listener.onAdministrationEvent(record(UPDATE_ROLE_ADMINISTRATION_USER_EVENT_PAYLOAD), acknowledgment)

        verifyOrder {
            userFinder.byId(USER_ID)
            tokenCommandHandler.handle(GENERATE_TOKEN_COMMAND)
            publishTokenAction(IDP_USER_ID, TOKEN)
            acknowledgment.acknowledge()
        }
        verify(exactly = 0) { acknowledgment.nack(any<Duration>()) }
    }

    @Test
    fun `must ignore unknown event and acknowledge message`() {
        listener.onAdministrationEvent(record("""{"type":"UNKNOWN"}"""), acknowledgment)

        verify(exactly = 1) { acknowledgment.acknowledge() }
        verify(exactly = 0) { userFinder.byId(any()) }
        verify(exactly = 0) { tokenCommandHandler.handle(any()) }
        verify(exactly = 0) { publishTokenAction(any(), any()) }
        verify(exactly = 0) { acknowledgment.nack(any<Duration>()) }
    }

    @Test
    fun `must nack invalid json`() {
        listener.onAdministrationEvent(record("not-json"), acknowledgment)

        verify(exactly = 0) { acknowledgment.acknowledge() }
        verify(exactly = 0) { userFinder.byId(any()) }
        verify(exactly = 0) { tokenCommandHandler.handle(any()) }
        verify(exactly = 0) { publishTokenAction(any(), any()) }
        verify(exactly = 1) { acknowledgment.nack(any<Duration>()) }
    }

    @Test
    fun `must nack when no user found`() {
        val unknownExternalUserId = 123L
        every { userFinder.byId(unknownExternalUserId) } returns null

        val payload = UPDATE_ROLE_ADMINISTRATION_USER_EVENT_PAYLOAD
            .replace(USER_ID.toString(), unknownExternalUserId.toString())
        listener.onAdministrationEvent(record(payload), acknowledgment)

        verify(exactly = 1) { userFinder.byId(unknownExternalUserId) }
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