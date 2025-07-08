package by.anatolyloyko.ams.studio.action

import by.anatolyloyko.ams.orm.jooq.schemas.routines.references.saveStudio
import by.anatolyloyko.ams.studio.model.Studio
import org.jooq.DSLContext
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Transactional
class JooqSaveStudioAction(
    private val dslContext: DSLContext,
) : SaveStudioAction {
    override fun invoke(studio: Studio): Long = saveStudio(
        configuration = dslContext.configuration(),
        iId = studio.id,
        iName = studio.name,
        iDescription = studio.description
    ) ?: error("Could not save the studio $studio")
}
