package by.anatolyloyko.ams.brand.action

import by.anatolyloyko.ams.brand.model.Brand

/**
 * Action responsible for creating a new brand.
 */
interface CreateBrandAction {
    /**
     * Creates a new brand and returns its ID.
     *
     * @param brand the brand data.
     * @return the ID of the created brand.
     */
    operator fun invoke(brand: Brand): Long
}
