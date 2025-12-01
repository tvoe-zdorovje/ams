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
     * @param ownerUserId identifier of a logged user. This user is considered the owner of the brand is created.
     * @return the ID of the saved brand.
     */
    operator fun invoke(brand: Brand, ownerUserId: Long): Long
}
