package by.anatolyloyko.ams.administration.brand.studio.action

import by.anatolyloyko.ams.administration.role.model.Role

/**
 * Action responsible for creating a new role for a studio.
 */
interface CreateStudioRoleAction {
    /**
     * Creates a new role for a studio and returns its ID.
     *
     * @param studioId the studio ID.
     * @param role the role data.
     * @param permissions the role permissions
     * @return the ID of the new role.
     */
    operator fun invoke(
        studioId: Long,
        role: Role,
        permissions: List<Long>
    ): Long
}
