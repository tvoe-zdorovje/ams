package by.anatolyloyko.ams.auth.token.finder

import by.anatolyloyko.ams.auth.token.model.TokenData

/**
 * Finder is responsible for finding necessary data for generating an access token.
 */
interface TokenDataFinder {
    /**
     * Finds necessary user data for generating an access token.
     *
     * @return user data required to generate an access token
     */
    fun findByUserId(userId: Long): TokenData
}
