package by.anatolyloyko.ams.studio.command

import by.anatolyloyko.ams.common.infrastructure.service.command.BaseCommandHandler
import by.anatolyloyko.ams.studio.action.SaveStudioAction
import org.springframework.stereotype.Component

/**
 * Handles {@link SaveStudioCommand}.
 *
 * Creates a new studio or updates an existing one based on the provided data.
 */
@Component
class SaveStudioCommandHandler(
    private val saveStudioAction: SaveStudioAction
) : BaseCommandHandler<SaveStudioCommand, Long>() {
    override fun handleInternal(command: SaveStudioCommand): Long = saveStudioAction(command.input)
}
