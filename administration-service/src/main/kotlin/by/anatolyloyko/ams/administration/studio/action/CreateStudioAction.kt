package by.anatolyloyko.ams.administration.studio.action

/**
 * Action responsible for creating a studio in the administration context.
 */
interface CreateStudioAction {
    /**
     * Creates a studio in the administration context.
     *
     * @param studioId the studio ID.
     * @param brandId the brand ID which the studio belongs to.
     * @param userId the user ID that owns the studio.
     *
     * @return the studio ID as a marker of successful creation.
     */
    operator fun invoke(studioId: Long, brandId: Long, userId: Long): Long
}
