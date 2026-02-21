package by.anatolyloyko.ams.administration.brand.action

import by.anatolyloyko.ams.orm.jooq.schemas.administration.routines.references.createBrand
import org.jooq.DSLContext
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Transactional
class JooqCreateBrandAction(
    private val dslContext: DSLContext
) : CreateBrandAction {
    override fun invoke(brandId: Long, userId: Long): Long = createBrand(
        configuration = dslContext.configuration(),
        iBrandId = brandId,
        iUserId = userId
    ) ?: throw IllegalStateException("Failed to create brand")
}
