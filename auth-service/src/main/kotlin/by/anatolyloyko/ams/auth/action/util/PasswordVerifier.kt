package by.anatolyloyko.ams.auth.action.util

/**
 * Interface intended to encapsulate specific implementation of password verification.
 */
interface PasswordVerifier {
    /**
     * Verifies a password against a hash.
     *
     * @param hash     hash.
     * @param password raw password.
     * @return True if the password matches the hash, false otherwise.
     */
    fun verify(password: CharArray, hash: String): Boolean
}
