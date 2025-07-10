package by.anatolyloyko.ams.administration.user.action

/**
 * Action responsible for assigning roles to a user.
 */
interface AssignRolesAction {
    /**
     * Assigns roles to a user.
     *
     * @param userId the target user ID.
     * @param organizationId either a brand ID or a studio ID which the roles belong to.
     * @param roles IDs of roles.
     */
    operator fun invoke(
        userId: Long,
        organizationId: Long,
        roles: List<Long>
    )
}
