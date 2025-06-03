package by.anatolyloyko.ams.auth.action

/**
 * Action responsible for user authorization.
 */
interface AuthorizeUserAction {
    /**
     * Authorizes a user by phone number and password and returns user's ID.
     *
     * @return authorized user ID
     * @throws ForbiddenException when a user with the specified phone number and password is not found.
     */
    operator fun invoke(phoneNumber: String, password: CharArray): Long
}
