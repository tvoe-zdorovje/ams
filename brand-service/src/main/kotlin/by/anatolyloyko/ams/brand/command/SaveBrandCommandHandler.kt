package by.anatolyloyko.ams.brand.command

import by.anatolyloyko.ams.brand.action.CreateBrandAction
import by.anatolyloyko.ams.brand.action.UpdateBrandAction
import by.anatolyloyko.ams.brand.kafka.KafkaProducer
import by.anatolyloyko.ams.common.infrastructure.service.command.BaseCommandHandler
import org.springframework.stereotype.Component

/**
 * Handles {@link CreateBrandCommand}.
 *
 * Creates a new brand or updates an existing one based on the provided data.
 */
@Component
class SaveBrandCommandHandler(
    private val dbCreateBrandAction: CreateBrandAction,
    private val dbUpdateBrandAction: UpdateBrandAction,
    private val kafkaProducer: KafkaProducer
) : BaseCommandHandler<SaveBrandCommand, Long>() {
    /**
     * Executes the command-specific logic for saving a brand.
     *
     * If the brand ID is null, creates a new brand in the database and sends a creation event to Kafka.
     * Otherwise, updates the existing brand in the database.
     *
     * @param command the command containing the brand data and user information.
     * @return the ID of the created or updated brand.
     */
    override fun handleInternal(command: SaveBrandCommand): Long = if (command.input.id == null) {
        val id = dbCreateBrandAction(command.input)
        kafkaProducer.sendBrandCreated(command.input.copy(id = id), command.loggedUserId)
    } else {
        dbUpdateBrandAction(command.input)
    }
}
