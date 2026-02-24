package by.anatolyloyko.ams.administration.studio.action

import by.anatolyloyko.ams.orm.jooq.schemas.administration.routines.references.createStudio
import org.jooq.DSLContext
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Transactional
class JooqCreateStudioAction(
    private val dslContext: DSLContext
) : CreateStudioAction {
    override fun invoke(studioId: Long, brandId: Long, userId: Long): Long = createStudio(
        configuration = dslContext.configuration(),
        iStudioId = studioId,
        iBrandId = brandId,
        iUserId = userId
    ) ?: throw IllegalStateException("Failed to create studio")
}
