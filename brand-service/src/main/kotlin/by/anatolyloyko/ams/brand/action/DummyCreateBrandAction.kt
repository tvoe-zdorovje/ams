package by.anatolyloyko.ams.brand.action

import by.anatolyloyko.ams.brand.database.DummyBrandRepository
import by.anatolyloyko.ams.brand.model.Brand
import org.springframework.stereotype.Component

/**
 * Temporary dummy implementation.
 */
@Component
class DummyCreateBrandAction(
    private val dummyBrandRepository: DummyBrandRepository
) : CreateBrandAction {
    override fun invoke(brand: Brand): Long = dummyBrandRepository.save(brand)
}
