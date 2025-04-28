package by.anatolyloyko.ams.administration.permission.finder

import by.anatolyloyko.ams.administration.permission.model.Permission

/**
 * Finder responsible for finding a permission.
 */
interface PermissionFinder {
    /**
     * Finds all permissions.
     *
     * @return found {@link Permission} list
     */
    fun findAll(): List<Permission>

    /**
     * Finds permissions by role {@link Role} ID.
     *
     * @param id the ID of the role.
     * @return found {@link Permission} list
     */
    fun findByRoleId(id: Long): List<Permission>
}
