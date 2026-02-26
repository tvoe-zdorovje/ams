package by.anatolyloyko.ams.studio.action

import by.anatolyloyko.ams.orm.jooq.schemas.studios.routines.references.updateStudio
import by.anatolyloyko.ams.studio.model.Studio
import org.jooq.DSLContext
import org.springframework.stereotype.Component

@Component("dbUpdateStudioAction")
class JooqUpdateStudioAction(
    private val dslContext: DSLContext,
) : UpdateStudioAction {
    override fun invoke(studio: Studio, userId: Long): Long = updateStudio(
        configuration = dslContext.configuration(),
        iId = requireNotNull(studio.id),
        iName = studio.name,
        iDescription = studio.description
    )
        ?: error("Could not save the studio $studio")
}
