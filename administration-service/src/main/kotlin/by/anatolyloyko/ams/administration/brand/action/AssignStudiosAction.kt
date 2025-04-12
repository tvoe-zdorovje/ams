package by.anatolyloyko.ams.administration.brand.action

/**
 * Action responsible for assigning studios to a brand.
 */
interface AssignStudiosAction {
    /**
     * Assigns studios to a brand.
     *
     * @param brandId the target brand ID.
     * @param studios IDs of studios
     */
    operator fun invoke(
        brandId: Long,
        studios: List<Long>
    )
}
