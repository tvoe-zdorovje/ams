package by.anatolyloyko.ams.studio.action

import by.anatolyloyko.ams.studio.model.Studio

/**
 * Action responsible for creating a new studio.
 */
interface SaveStudioAction {
    /**
     * Creates a new studio and returns the generated ID.
     *
     * @param studio the studio data.
     * @return the ID of the newly created studio.
     */
    operator fun invoke(studio: Studio): Long
}
