package by.anatolyloyko.ams.user.kafka

import by.anatolyloyko.ams.user.action.CreateUserAction
import by.anatolyloyko.ams.user.model.User
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.mockk.mockk
import io.mockk.verify
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.assertj.core.api.WithAssertions
import org.junit.jupiter.api.Test
import org.springframework.kafka.support.Acknowledgment
import java.time.Duration

class KeycloakEventsKafkaListenerTest : WithAssertions {
    private val createUserAction = mockk<CreateUserAction>(relaxed = true)

    private val listener = KeycloakEventsKafkaListener(
        objectMapper = jacksonObjectMapper(),
        createUserAction = createUserAction,
    )

    private val acknowledgment = mockk<Acknowledgment>(relaxed = true)

    @Test
    fun `must create user and acknowledge message`() {
        val firstName = "Daneel"
        val lastName = "Kolbasenko"
        val phoneNumber = "+375297925571"
        val userId = """708e421b-d2c5-42b9-9114-dc2b9109bb49"""
        val payload = """
            {
              "id": "89cfaf81-9790-4cf1-b459-1c35efa82e0f",
              "time": 1786369611495,
              "type": "REGISTER",
              "realmId": "a14981c9-06d4-4e1f-ba55-a445dbb818e5",
              "realmName": "ams",
              "clientId": "gateway-client-id",
              "userId": "$userId",
              "details": {
                "first_name": "$firstName",
                "last_name": "$lastName",
                "username": "$phoneNumber"
              }
            }
        """.trimIndent()

        listener.onKeycloakEvent(record(payload), acknowledgment)

        verify {
            createUserAction(
                user = User(
                    firstName = firstName,
                    lastName = lastName,
                    phoneNumber = phoneNumber,
                ),
                externalId = userId,
            )

            acknowledgment.acknowledge()
        }
        verify(exactly = 0) { acknowledgment.nack(any<Duration>()) }
    }

    @Test
    fun `must ignore non register event and acknowledge message`() {
        val payload = """{"type":"LOGIN"}"""

        listener.onKeycloakEvent(record(payload), acknowledgment)

        verify(exactly = 1) { acknowledgment.acknowledge() }
        verify(exactly = 0) { createUserAction(any(), any()) }
        verify(exactly = 0) { acknowledgment.nack(any<Duration>()) }
    }

    @Test
    fun `must nack invalid json`() {
        listener.onKeycloakEvent(record("not-json"), acknowledgment)

        verify(exactly = 0) { acknowledgment.acknowledge() }
        verify(exactly = 0) { createUserAction(any(), any()) }
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
