package by.anatolyloyko.ams.studio.action

import by.anatolyloyko.ams.studio.model.Studio

/**
 * Action responsible for updating a studio.
 */
interface UpdateStudioAction {
    /**
     * Updates a studio and returns its ID.
     *
     * @param studio the studio data.
     * @param userId identifier of a logged user who initiated the update operation.
     *
     * @return the ID of the updated studio.
     */
    operator fun invoke(studio: Studio, userId: Long): Long
}
