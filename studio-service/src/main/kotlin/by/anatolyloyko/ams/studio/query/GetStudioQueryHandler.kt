package by.anatolyloyko.ams.studio.query

import by.anatolyloyko.ams.infrastructure.service.query.BaseQueryHandler
import by.anatolyloyko.ams.studio.finder.StudioFinder
import by.anatolyloyko.ams.studio.model.Studio
import org.springframework.stereotype.Component

/**
 * Handles {@link GetStudioQuery}.
 *
 * Find a studio based on the provided data.
 */
@Component
class GetStudioQueryHandler(
    private val studioFinder: StudioFinder,
) : BaseQueryHandler<GetStudioQuery, Studio?>() {
    override fun handleInternal(query: GetStudioQuery): Studio? = studioFinder.findById(query.input)
}
