package by.anatolyloyko.ams.auth.user.finder

import by.anatolyloyko.ams.auth.user.model.User

/**
 * Finder responsible for finder user data.
 */
interface UserFinder {
    /**
     * Finds user data by internal user ID.
     *
     * @return found user data or null
     */
    fun byId(id: Long): User?
    /**
     * Finds user data by external Identity Provider UUID.
     *
     * @return found user data or null
     */
    fun byIdpUUID(idpUUID: String): User?
}
