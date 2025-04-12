package by.anatolyloyko.ams.administration.user.action

/**
 * Action responsible for unassigning roles from a user.
 */
interface UnassignRolesAction {
    /**
     * Unassigns roles from a user.
     *
     * @param userId the target user ID.
     * @param roles IDs of roles.
     */
    operator fun invoke(
        userId: Long,
        roles: List<Long>
    )
}
