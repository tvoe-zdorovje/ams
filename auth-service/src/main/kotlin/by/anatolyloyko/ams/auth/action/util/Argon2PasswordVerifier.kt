package by.anatolyloyko.ams.auth.action.util

import de.mkammerer.argon2.Argon2Factory
import org.springframework.stereotype.Component

/**
 * The implementation using the Argon2 algorithm to check a password.
 */
@Component
class Argon2PasswordVerifier : PasswordVerifier {
    private val argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id)

    /**
     * {@inheritDoc}
     */
    override fun verify(password: CharArray, hash: String): Boolean = argon2.verify(hash, password)
}
