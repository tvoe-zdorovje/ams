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
     * @param ownerUserId identifier of a logged user. This user is considered the owner of the brand is created.
     * @return the ID of the created brand.
     */
    operator fun invoke(brand: Brand, ownerUserId: Long): Long
}
