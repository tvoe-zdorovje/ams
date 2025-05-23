package by.anatolyloyko.ams.auth.token.command

interface TokenCommandHandler {
    fun handle(command: GenerateTokenCommand): String
}
