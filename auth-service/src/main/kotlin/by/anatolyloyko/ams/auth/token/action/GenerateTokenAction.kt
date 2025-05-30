package by.anatolyloyko.ams.auth.token.action

import by.anatolyloyko.ams.auth.token.model.TokenData

interface GenerateTokenAction {
    operator fun invoke(tokenData: TokenData): String
}
