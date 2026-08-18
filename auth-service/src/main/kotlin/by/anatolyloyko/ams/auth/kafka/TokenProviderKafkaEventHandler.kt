package by.anatolyloyko.ams.auth.kafka

import by.anatolyloyko.ams.auth.token.action.PublishTokenAction
import by.anatolyloyko.ams.auth.token.command.GenerateTokenCommand
import by.anatolyloyko.ams.auth.token.command.TokenCommandHandler
import by.anatolyloyko.ams.auth.token.command.input.GenerateTokenCommandInput
import by.anatolyloyko.ams.auth.user.model.User
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.support.Acknowledgment
import java.time.Duration

/**
 * Base Kafka event handler for events that require issuing and publishing a token for a user.
 *
 * Implements the common event processing algorithm:
 * - Parse Kafka record payload as JSON;
 * - Ignore and acknowledge events with unsupported event types;
 * - Extract user ID from the event payload;
 * - Find the corresponding user;
 * - Generate a token and publish it.
 *
 * Subclasses provide event-source specific strategies for extracting event data and resolving users.
 *
 * @param ID_TYPE type of user identifier extracted from the event payload.
 * @property eventTypes event types supported by this handler.
 */
abstract class TokenProviderKafkaEventHandler<ID_TYPE>(
    private val objectMapper: ObjectMapper,
    private val tokenCommandHandler: TokenCommandHandler,
    private val publishTokenAction: PublishTokenAction,
    private val eventTypes: List<String>,
) {
    private val log = LoggerFactory.getLogger(this::class.java)

    @Value("\${ams.kafka.props.nack-cooldown}")
    private var nackCooldown: Duration = Duration.ZERO

    /**
     * Finds a user by the identifier extracted from the event payload.
     *
     * @return found user or `null` if no matching user exists.
     */
    abstract fun findUser(userId: ID_TYPE): User?

    /**
     * Extracts the user identifier from the event payload.
     */
    abstract fun extractUserId(payload: JsonNode): ID_TYPE

    /**
     * Extracts the event type from the event payload.
     */
    abstract fun extractEventType(payload: JsonNode): String

    /**
     * Checks whether this handler supports the given event type.
     */
    fun canHandle(eventType: String): Boolean = eventTypes.contains(eventType)

    /**
     * Handles a Kafka event record by generating and publishing a token for the event user.
     *
     * Acknowledges unsupported and successfully processed events.
     * Negatively acknowledges failed events using the configured cooldown.
     */
    fun handleEvent(
        record: ConsumerRecord<String, String>,
        acknowledgment: Acknowledgment
    ) {
        try {
            val payload = objectMapper.readTree(record.value())
            val eventType = extractEventType(payload)
            if (!canHandle(eventType)) {
                return acknowledgment.acknowledge()
            }

            val userId = extractUserId(payload)

            log.info(
                "Consumed event [type={}, topic={}, key={}, user ID={}]",
                eventType,
                record.topic(),
                record.key(),
                userId
            )

            val user = findUserInternal(userId)
            publishTokenAction(
                key = user.idpUUID,
                token = generateToken(
                    user = user,
                )
            )

            acknowledgment.acknowledge()
        } catch (e: Exception) {
            log.error("Failed to process an event [key=${record.key()}]", e)
            acknowledgment.nack(nackCooldown)
        }
    }

    private fun findUserInternal(userId: ID_TYPE): User = findUser(userId)
        ?: throw RuntimeException("No user found with ID = $userId")

    private fun generateToken(user: User): String = tokenCommandHandler.handle(
        GenerateTokenCommand(
            input = GenerateTokenCommandInput(user)
        )
    )
}
