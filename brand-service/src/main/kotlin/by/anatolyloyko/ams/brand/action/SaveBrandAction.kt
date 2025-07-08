package by.anatolyloyko.ams.brand.action

import by.anatolyloyko.ams.brand.model.Brand

/**
 * Action responsible for creating a new brand or updating an existing one.
 */
interface SaveBrandAction {
    /**
     * Saves a brand and returns its ID.
     *
     * @param brand the brand data.
     * @return the ID of the saved brand.
     */
    operator fun invoke(brand: Brand): Long
}
