package by.anatolyloyko.ams.studio.finder

import by.anatolyloyko.ams.orm.jooq.schemas.tables.records.StudioRecord
import by.anatolyloyko.ams.orm.jooq.schemas.tables.references.STUDIO
import by.anatolyloyko.ams.orm.jooq.util.eq
import by.anatolyloyko.ams.studio.model.Studio
import org.jooq.DSLContext
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

private val MAPPER: (StudioRecord) -> Studio = {
    Studio(
        id = it.id,
        name = it.name,
        description = it.description
    )
}

@Component
@Transactional(readOnly = true)
class JooqStudioFinder(
    private val dslContext: DSLContext
) : StudioFinder {
    override fun findById(id: Long): Studio? = dslContext
        .selectFrom(STUDIO)
        .where(STUDIO.ID eq id)
        .fetchOne(MAPPER)
}
