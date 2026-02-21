package by.anatolyloyko.ams.administration.brand.action

/**
 * Action responsible for creating a brand in the administration context.
 */
interface CreateBrandAction {
    /**
     * Creates a brand in the administration context.
     *
     * @param brandId the brand ID.
     * @param userId the user ID that owns the brand.
     *
     * @return the brand ID as a marker of successful creation.
     */
    operator fun invoke(brandId: Long, userId: Long): Long
}
