package by.anatolyloyko.ams.studio.finder

import by.anatolyloyko.ams.studio.model.Studio

/**
 * Finder responsible for finding a studio.
 */
interface StudioFinder {
    /**
     * Finds a studio by ID.
     *
     * @param id the ID of the studio.
     * @return the found {@link Studio}, or `null` if no studio is found.
     */
    fun findById(id: Long): Studio?
}
