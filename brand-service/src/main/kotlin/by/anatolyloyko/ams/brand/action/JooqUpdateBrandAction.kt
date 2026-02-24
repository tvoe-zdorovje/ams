package by.anatolyloyko.ams.brand.action

import by.anatolyloyko.ams.brand.model.Brand
import by.anatolyloyko.ams.orm.jooq.schemas.brands.routines.references.updateBrand
import org.jooq.DSLContext
import org.springframework.stereotype.Component

@Component
internal class JooqUpdateBrandAction(
    private val dslContext: DSLContext
) : UpdateBrandAction {
    override fun invoke(brand: Brand): Long = updateBrand(
        configuration = dslContext.configuration(),
        iId = requireNotNull(brand.id),
        iName = brand.name,
        iDescription = brand.description
    )
        ?: error("Could not save the brand $brand")
}
