package by.anatolyloyko.ams.studio.command

import by.anatolyloyko.ams.infrastructure.service.command.BaseCommandHandler
import by.anatolyloyko.ams.studio.action.CreateStudioAction
import org.springframework.stereotype.Component

/**
 * Handles {@link CreateStudioCommand}.
 *
 * Creates a new studio based on the provided data.
 */
@Component
class CreateStudioCommandHandler(
    private val createStudioAction: CreateStudioAction
) : BaseCommandHandler<CreateStudioCommand, Long>() {
    override fun handleInternal(command: CreateStudioCommand): Long = createStudioAction(command.input)
}
