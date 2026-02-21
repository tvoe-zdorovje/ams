package by.anatolyloyko.ams.brand.command

import by.anatolyloyko.ams.brand.action.CreateBrandAction
import by.anatolyloyko.ams.brand.action.UpdateBrandAction
import by.anatolyloyko.ams.common.infrastructure.service.command.BaseCommandHandler
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

/**
 * Handles {@link CreateBrandCommand}.
 *
 * Creates a new brand or updates an existing one based on the provided data.
 */
@Component
class SaveBrandCommandHandler(
    private val dbCreateBrandAction: CreateBrandAction,
    private val dbUpdateBrandAction: UpdateBrandAction,
    private val kafkaCreateBrandAction: CreateBrandAction
) : BaseCommandHandler<SaveBrandCommand, Long>() {
    override fun handleInternal(command: SaveBrandCommand): Long = if (command.input.id == null) {
        val id = dbCreateBrandAction(command.input, command.loggedUserId)
        kafkaCreateBrandAction(command.input.copy(id = id), command.loggedUserId)
    } else {
        dbUpdateBrandAction(command.input)
    }
}
