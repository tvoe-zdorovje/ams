package by.anatolyloyko.ams.administration.role.finder

import by.anatolyloyko.ams.administration.role.model.Role

/**
 * Finder responsible for finding a role.
 */
interface RoleFinder {
    /**
     * Finds a role by ID.
     *
     * @param id the ID of the role.
     * @return the found {@link Role}, or `null` if no role is found.
     */
    fun findById(id: Long): Role?
}
