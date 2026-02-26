package by.anatolyloyko.ams.brand.action

import by.anatolyloyko.ams.brand.model.Brand
import by.anatolyloyko.ams.orm.jooq.schemas.brands.routines.references.createBrand
import org.jooq.DSLContext
import org.springframework.stereotype.Component

@Component("dbCreateBrandAction")
internal class JooqCreateBrandAction(
    private val dslContext: DSLContext
) : CreateBrandAction {
    override fun invoke(brand: Brand): Long = createBrand(
        configuration = dslContext.configuration(),
        iName = brand.name,
        iDescription = brand.description
    )
        ?: error("Could not save the brand $brand")
}
