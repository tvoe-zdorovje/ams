package by.anatolyloyko.ams.auth.token.model

data class TokenData(
    val userId: Long,
    val permissions: Map<Long, List<Permission>>
) {
    /**
     * Converts [permissions] into map of organization ID to set of permission names
     */
    fun getPermissionsMap(): Map<Long, List<String>> = permissions.mapValues { entry ->
        entry.value.map(Permission::name)
    }
}
