package by.anatolyloyko.ams.studio.command

import by.anatolyloyko.ams.common.infrastructure.service.command.BaseCommandHandler
import by.anatolyloyko.ams.studio.action.CreateStudioAction
import by.anatolyloyko.ams.studio.action.UpdateStudioAction
import by.anatolyloyko.ams.studio.kafka.KafkaProducer
import org.springframework.stereotype.Component

/**
 * Handles {@link SaveStudioCommand}.
 *
 * Creates a new studio or updates an existing one based on the provided data.
 */
@Component
class SaveStudioCommandHandler(
    private val dbCreateStudioAction: CreateStudioAction,
    private val dbUpdateStudioAction: UpdateStudioAction,
    private val kafkaProducer: KafkaProducer,
) : BaseCommandHandler<SaveStudioCommand, Long>() {

    /**
     * Executes the command-specific logic for saving a studio.
     *
     * If the studio ID is null, creates a new studio in the database and sends a creation event to Kafka.
     * Otherwise, updates the existing studio in the database.
     *
     * @param command the command containing the studio data and user information.
     * @return the ID of the created or updated studio.
     */
    override fun handleInternal(command: SaveStudioCommand): Long = if (command.input.id == null) {
        val id = dbCreateStudioAction(command.input)
        kafkaProducer.sendStudioCreated(
            command.input.copy(id = id),
            command.loggedUserId,
            command.brandId
        )
    } else {
        dbUpdateStudioAction(command.input, command.loggedUserId)
    }
}
