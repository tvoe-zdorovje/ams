package by.anatolyloyko.ams.studio.action

import by.anatolyloyko.ams.orm.jooq.schemas.studios.routines.references.createStudio
import by.anatolyloyko.ams.orm.jooq.schemas.studios.routines.references.updateStudio
import by.anatolyloyko.ams.studio.model.Studio
import org.jooq.DSLContext
import org.springframework.stereotype.Component

@Component
class JooqSaveStudioAction(
    private val dslContext: DSLContext,
) : SaveStudioAction {
    override fun invoke(studio: Studio, ownerUserId: Long): Long = if (studio.id == null) {
        createStudio(
            configuration = dslContext.configuration(),
            iName = studio.name,
            iDescription = studio.description,
            iOwnerUserId = ownerUserId
        )
    } else {
        updateStudio(
            configuration = dslContext.configuration(),
            iId = studio.id,
            iName = studio.name,
            iDescription = studio.description
        )
    }
        ?: error("Could not save the studio $studio")
}
