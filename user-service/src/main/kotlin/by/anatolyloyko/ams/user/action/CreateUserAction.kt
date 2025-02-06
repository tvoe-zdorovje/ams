package by.anatolyloyko.ams.user.action

import by.anatolyloyko.ams.user.model.User

/**
 * Action responsible for creating a new user.
 */
interface CreateUserAction {
    /**
     * Creates a new user and returns the generated ID.
     *
     * @param user the user data.
     * @return the ID of the newly created user.
     */
    operator fun invoke(user: User): Long
}
