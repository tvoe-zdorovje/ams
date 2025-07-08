package by.anatolyloyko.ams.brand.action

import by.anatolyloyko.ams.brand.model.Brand
import by.anatolyloyko.ams.orm.jooq.schemas.routines.references.saveBrand
import org.jooq.DSLContext
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Transactional
internal class JooqSaveBrandAction(
    private val dslContext: DSLContext
) : SaveBrandAction {
    override fun invoke(brand: Brand): Long = saveBrand(
        configuration = dslContext.configuration(),
        iId = brand.id,
        iName = brand.name,
        iDescription = brand.description
    ) ?: error("Could not save the brand $brand")
}
