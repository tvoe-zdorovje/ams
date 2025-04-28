package by.anatolyloyko.ams.administration.role.action

import by.anatolyloyko.ams.administration.role.model.Role

/**
 * Action responsible for saving a role.
 */
interface SaveRoleAction {
    /**
     * Saves a role and returns its ID. If the role is new (ID is null), then generates a new ID.
     *
     * @param role the role data.
     * @param permissions the role permissions
     * @return the ID of the role.
     */
    operator fun invoke(
        role: Role,
        permissions: List<Long> = emptyList()
    ): Long
}
