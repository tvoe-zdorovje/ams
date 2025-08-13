package by.anatolyloyko.ams.administration.role.action

import by.anatolyloyko.ams.administration.role.model.Role

/**
 * Action responsible for saving a role.
 */
interface SaveRoleAction {
    /**
     * Saves a role and returns its ID. If the role is new (ID is null), then generates a new ID.
     *
     * @param organizationId either a brand ID or a studio ID which the role belong to.
     * @param role the role data.
     * @param permissions the role permissions
     * @return the ID of the role.
     */
    operator fun invoke(
        organizationId: Long,
        role: Role,
        permissions: List<Long> = emptyList()
    ): Long
}
