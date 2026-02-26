package by.anatolyloyko.ams.studio.action

import by.anatolyloyko.ams.studio.model.Studio

/**
 * Action responsible for creating a new studio.
 */
interface CreateStudioAction {
    /**
     * Creates a new studio and returns its ID.
     *
     * @param studio the studio data.
     * @return the ID of the created studio.
     */
    operator fun invoke(studio: Studio): Long
}
