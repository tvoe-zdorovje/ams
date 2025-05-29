package by.anatolyloyko.ams.auth.action.util

import de.mkammerer.argon2.Argon2Factory
import org.springframework.stereotype.Component

@Component
class Argon2PasswordVerifier : PasswordVerifier {
    private val argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id)

    override fun verify(password: CharArray, hash: String): Boolean = argon2.verify(hash, password)
}
