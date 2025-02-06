package by.anatolyloyko.ams.studio.action

import by.anatolyloyko.ams.studio.database.DummyStudioRepository
import by.anatolyloyko.ams.studio.model.Studio
import org.springframework.stereotype.Component

/**
 * Temporary dummy implementation.
 */
@Component
class DummyCreateStudioAction(
    private val dummyStudioRepository: DummyStudioRepository
) : CreateStudioAction {
    override fun invoke(studio: Studio): Long = dummyStudioRepository.save(studio)
}
