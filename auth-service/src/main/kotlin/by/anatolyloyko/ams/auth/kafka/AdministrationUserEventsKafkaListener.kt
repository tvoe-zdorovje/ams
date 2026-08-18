package by.anatolyloyko.ams.auth.kafka

import by.anatolyloyko.ams.auth.token.action.PublishTokenAction
import by.anatolyloyko.ams.auth.token.command.TokenCommandHandler
import by.anatolyloyko.ams.auth.user.finder.UserFinder
import by.anatolyloyko.ams.auth.user.model.User
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.Acknowledgment
import org.springframework.stereotype.Component

private const val USER_ID_PARAM_NAME = "userId"

private const val EVENT_TYPE_PARAM_NAME = "type"

/**
 * Kafka Consumer listening events produced by the Administration Service
 */
@Component
class AdministrationUserEventsKafkaListener(
    private val userFinder: UserFinder,
    objectMapper: ObjectMapper,
    tokenCommandHandler: TokenCommandHandler,
    publishTokenAction: PublishTokenAction,
    @Value("#{'\${ams.kafka.topic.administration-user.event-types}'.split(',')}")
    eventTypes: List<String>
): TokenProviderKafkaEventHandler<Long>(objectMapper, tokenCommandHandler, publishTokenAction, eventTypes) {

    /**
     * Kafka Listener handling events from administration-events topic.
     *
     * Accepts [eventTypes] events, generates token containing data about the user and publishes it to Redis.
     */
    @KafkaListener(topics = ["\${ams.kafka.topic.administration-user.name}"])
    fun onAdministrationEvent(
        record: ConsumerRecord<String, String>,
        acknowledgment: Acknowledgment
    ) = handleEvent(record, acknowledgment)

    override fun findUser(userId: Long): User? = userFinder.byId(userId)

    override fun extractUserId(payload: JsonNode): Long = payload.path(USER_ID_PARAM_NAME).asLong()

    override fun extractEventType(payload: JsonNode): String = payload.path(EVENT_TYPE_PARAM_NAME).asText()
}
