package by.anatolyloyko.ams.studio.finder

import by.anatolyloyko.ams.studio.database.DummyStudioRepository
import by.anatolyloyko.ams.studio.model.Studio
import org.springframework.stereotype.Component

/**
 * Temporary dummy implementation
 */
@Component
class DummyStudioFinder(
    private val dummyStudioRepository: DummyStudioRepository
) : StudioFinder {
    override fun findById(id: Long): Studio? = dummyStudioRepository.findById(id)
}
