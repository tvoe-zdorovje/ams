package by.anatolyloyko.ams.user.password.action

/**
 * Action responsible for hashing a password.
 */
interface HashPasswordAction {
    /**
     * Hashes and returns a password.
     *
     * @param rawPassword raw password char array.
     * @return hashed password
     */
    operator fun invoke(rawPassword: CharArray): String
}
