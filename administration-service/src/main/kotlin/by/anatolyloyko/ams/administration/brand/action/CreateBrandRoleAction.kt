package by.anatolyloyko.ams.administration.brand.action

import by.anatolyloyko.ams.administration.role.model.Role

/**
 * Action responsible for creating a new role for a brand.
 */
interface CreateBrandRoleAction {
    /**
     * Creates a new role for a brand and returns its ID.
     *
     * @param brandId the brand ID.
     * @param role the role data.
     * @param permissions the role permissions
     * @return the ID of the new role.
     */
    operator fun invoke(
        brandId: Long,
        role: Role,
        permissions: List<Long>
    ): Long
}
