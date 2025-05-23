package by.anatolyloyko.ams.user.password.action

import de.mkammerer.argon2.Argon2Factory
import de.mkammerer.argon2.Argon2Helper
import org.springframework.stereotype.Component

private const val TIME_LIMIT = 1000L

private const val MEMORY = 65536

private const val PARALLELISM = 2

@Component
class Argon2HashPasswordAction : HashPasswordAction {
    private val argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id)

    private val iterations = Argon2Helper.findIterations(argon2, TIME_LIMIT, MEMORY, PARALLELISM)

    override fun invoke(rawPassword: CharArray): String = argon2.hash(iterations, MEMORY, PARALLELISM, rawPassword)
}
