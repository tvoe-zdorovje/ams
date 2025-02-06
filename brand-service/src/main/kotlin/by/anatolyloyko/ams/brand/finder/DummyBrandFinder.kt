package by.anatolyloyko.ams.brand.finder

import by.anatolyloyko.ams.brand.database.DummyBrandRepository
import by.anatolyloyko.ams.brand.model.Brand
import org.springframework.stereotype.Component

/**
 * Temporary dummy implementation
 */
@Component
class DummyBrandFinder(
    private val dummyBrandRepository: DummyBrandRepository
) : BrandFinder {
    override fun findById(id: Long): Brand? = dummyBrandRepository.findById(id)
}
