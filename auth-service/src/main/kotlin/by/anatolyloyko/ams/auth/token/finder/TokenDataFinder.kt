package by.anatolyloyko.ams.auth.token.finder

import by.anatolyloyko.ams.auth.token.model.TokenData

interface TokenDataFinder {
    fun findByUserId(userId: Long): TokenData?
}
