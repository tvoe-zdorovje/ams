package by.anatolyloyko.ams.user.command.input

import by.anatolyloyko.ams.user.model.User

data class CreateUserCommandInput(
    val user: User,
    val password: CharArray,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CreateUserCommandInput

        return user == other.user
    }

    override fun hashCode(): Int {
        return user.hashCode()
    }

    override fun toString(): String {
        return "CreateUserCommandInput(user=$user)"
    }
}
