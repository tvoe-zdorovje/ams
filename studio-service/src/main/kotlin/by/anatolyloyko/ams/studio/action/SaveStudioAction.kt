package by.anatolyloyko.ams.studio.action

import by.anatolyloyko.ams.studio.model.Studio

/**
 * Action responsible for creating a new studio or updating an existing one.
 */
interface SaveStudioAction {
    /**
     * Saves a studio and returns its ID.
     *
     * @param studio the studio data.
     * @param ownerUserId identifier of a logged user. This user is considered the owner of the studio is created.
     * @return the ID of the saved studio.
     */
    operator fun invoke(studio: Studio, ownerUserId: Long): Long
}
