package by.anatolyloyko.ams.auth.token.action

import by.anatolyloyko.ams.auth.token.model.TokenData

/**
 * Action responsible for generation tokens.
 */
interface GenerateTokenAction {
    operator fun invoke(tokenData: TokenData): String
}
