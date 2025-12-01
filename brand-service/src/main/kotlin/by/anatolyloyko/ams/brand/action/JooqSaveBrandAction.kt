package by.anatolyloyko.ams.brand.action

import by.anatolyloyko.ams.brand.model.Brand
import by.anatolyloyko.ams.orm.jooq.schemas.brands.routines.references.createBrand
import by.anatolyloyko.ams.orm.jooq.schemas.brands.routines.references.updateBrand
import org.jooq.DSLContext
import org.springframework.stereotype.Component

@Component
internal class JooqSaveBrandAction(
    private val dslContext: DSLContext
) : SaveBrandAction {
    override fun invoke(brand: Brand, ownerUserId: Long): Long = if (brand.id == null) {
        createBrand(
            configuration = dslContext.configuration(),
            iName = brand.name,
            iDescription = brand.description,
            iOwnerUserId = ownerUserId
        )
    } else {
        updateBrand(
            configuration = dslContext.configuration(),
            iId = brand.id,
            iName = brand.name,
            iDescription = brand.description
        )
    }
        ?: error("Could not save the brand $brand")
}
