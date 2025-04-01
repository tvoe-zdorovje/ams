package by.anatolyloyko.ams.brand.finder

import by.anatolyloyko.ams.brand.model.Brand
import by.anatolyloyko.ams.orm.jooq.tables.records.BrandRecord
import by.anatolyloyko.ams.orm.jooq.tables.references.BRAND
import by.anatolyloyko.ams.orm.jooq.util.eq
import org.jooq.DSLContext
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

private val MAPPER: (BrandRecord) -> Brand = {
    Brand(
        id = it.id,
        name = it.name,
        description = it.description
    )
}

@Component
@Transactional(readOnly = true)
class JooqBrandFinder(
    private val dsl: DSLContext
) : BrandFinder {
    override fun findById(id: Long): Brand? = dsl
        .selectFrom(BRAND)
        .where(BRAND.ID eq id)
        .fetchOne(MAPPER)
}
