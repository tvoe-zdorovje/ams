package by.anatolyloyko.ams.auth.action.util

/**
 * Util encapsulates the password verification process.
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
