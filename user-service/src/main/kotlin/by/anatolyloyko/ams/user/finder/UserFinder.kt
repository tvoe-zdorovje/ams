package by.anatolyloyko.ams.user.finder

import by.anatolyloyko.ams.user.model.User

/**
 * Finder responsible for finding a user.
 */
interface UserFinder {
    /**
     * Finds a user by ID.
     *
     * @param id the ID of the user.
     * @return the found {@link User}, or `null` if no user is found.
     */
    fun findById(id: Long): User?
}
