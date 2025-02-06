package by.anatolyloyko.ams.brand.query

import by.anatolyloyko.ams.brand.finder.BrandFinder
import by.anatolyloyko.ams.brand.model.Brand
import by.anatolyloyko.ams.infrastructure.service.query.BaseQueryHandler
import org.springframework.stereotype.Component

/**
 * Handles {@link GetBrandQuery}.
 *
 * Find a brand based on the provided data.
 */
@Component
class GetBrandQueryHandler(
    private val brandFinder: BrandFinder,
) : BaseQueryHandler<GetBrandQuery, Brand?>() {
    override fun handleInternal(query: GetBrandQuery): Brand? = brandFinder.findById(query.input)
}
