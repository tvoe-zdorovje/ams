package by.anatolyloyko.ams.studio.action

import by.anatolyloyko.ams.orm.jooq.schemas.studios.routines.references.createStudio
import by.anatolyloyko.ams.studio.model.Studio
import org.jooq.DSLContext
import org.springframework.stereotype.Component

@Component("dbCreateStudioAction")
class JooqCreateStudioAction(
    private val dslContext: DSLContext,
) : CreateStudioAction {
    override fun invoke(studio: Studio): Long = createStudio(
        configuration = dslContext.configuration(),
        iName = studio.name,
        iDescription = studio.description
    )
        ?: error("Could not save the studio $studio")
}
