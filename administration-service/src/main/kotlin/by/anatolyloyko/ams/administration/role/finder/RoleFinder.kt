package by.anatolyloyko.ams.administration.role.finder

import by.anatolyloyko.ams.administration.role.model.Role

/**
 * Finder responsible for finding a role.
 */
interface RoleFinder {
    /**
     * Finds a role by ID.
     *
     * @param roleId the ID of a role.
     * @param organizationId the ID of an organization which the role belongs to.
     * @return the found [Role], or `null` if no role is found.
     */
    fun findById(roleId: Long, organizationId: Long): Role?
}
