package by.anatolyloyko.ams.administration.user.action

/**
 * Action responsible for assigning roles to a user.
 */
interface AssignRolesAction {
    /**
     * Assigns roles to a user.
     *
     * @param userId the target user ID.
     * @param roles IDs of roles.
     */
    operator fun invoke(
        userId: Long,
        roles: List<Long>
    )
}
