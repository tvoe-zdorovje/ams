package by.anatolyloyko.ams.brand.action

import by.anatolyloyko.ams.brand.model.Brand

/**
 * Action responsible for updating a brand.
 */
interface UpdateBrandAction {
    /**
     * Updates a brand and returns its ID.
     *
     * @param brand the brand data.
     * @return the ID of the updated brand.
     */
    operator fun invoke(brand: Brand): Long
}
