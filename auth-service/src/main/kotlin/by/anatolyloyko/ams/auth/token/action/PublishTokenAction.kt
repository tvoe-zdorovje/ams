package by.anatolyloyko.ams.auth.token.action

/**
 * Action responsible for publishing tokens to REmote DIctionary Servers (like Redis, lol).
 */
interface PublishTokenAction {
    /**
     * Publishes pair of prefix:[key]<->[token].
     */
    operator fun invoke(key: String, token: String)
}
