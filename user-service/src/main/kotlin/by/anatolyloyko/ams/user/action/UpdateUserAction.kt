package by.anatolyloyko.ams.user.action

import by.anatolyloyko.ams.user.model.User

/**
 * Action responsible for updating a user.
 */
interface UpdateUserAction {
    /**
     * Updates a user and returns his ID.
     *
     * @param user updated user data.
     * @return the ID of the updated user.
     */
    operator fun invoke(user: User): Long
}
