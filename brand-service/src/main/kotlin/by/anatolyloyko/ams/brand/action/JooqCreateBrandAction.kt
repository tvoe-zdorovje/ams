package by.anatolyloyko.ams.brand.action

import by.anatolyloyko.ams.brand.model.Brand
import by.anatolyloyko.ams.orm.jooq.routines.references.saveBrand
import org.jooq.DSLContext
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Transactional // TODO impl UT
internal class JooqCreateBrandAction(
    private val dslContext: DSLContext
) : CreateBrandAction {
    override fun invoke(brand: Brand): Long = saveBrand(
        configuration = dslContext.configuration(),
        iId = brand.id,
        iName = brand.name,
        iDescription = brand.description
    ) ?: error("Could not create a new brand $brand")
}
