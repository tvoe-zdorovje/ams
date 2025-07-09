package by.anatolyloyko.ams.administration.user.finder

import by.anatolyloyko.ams.administration.role.model.Role

/**
 * Finder responsible for finding user roles.
 */
interface UserRolesFinder {
    /**
     * Finds user roles by organization ID.
     *
     * @param userId the user ID.
     * @param organizationId either a brand ID or a studio ID which roles belong to.
     * @return found [Role] list.
     */
    fun findByOrganizationId(
        userId: Long,
        organizationId: Long
    ): List<Role>
}
