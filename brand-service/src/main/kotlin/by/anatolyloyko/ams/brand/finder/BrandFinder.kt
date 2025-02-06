package by.anatolyloyko.ams.brand.finder

import by.anatolyloyko.ams.brand.model.Brand

/**
 * Finder responsible for finding a brand.
 */
interface BrandFinder {
    /**
     * Finds a brand by ID.
     *
     * @param id the ID of the brand.
     * @return the found {@link Brand}, or `null` if no brand is found.
     */
    fun findById(id: Long): Brand?
}
