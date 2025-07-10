package by.anatolyloyko.ams.administration.brand.action

import by.anatolyloyko.ams.orm.jooq.schemas.administration.routines.references.addBrandStudios
import org.jooq.DSLContext
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Transactional
class JooqAssignStudiosAction(
    private val dslContext: DSLContext
) : AssignStudiosAction {
    override fun invoke(brandId: Long, studios: List<Long>) {
        addBrandStudios(
            configuration = dslContext.configuration(),
            iBrandId = brandId,
            iStudios = studios.toTypedArray()
        )
    }
}
