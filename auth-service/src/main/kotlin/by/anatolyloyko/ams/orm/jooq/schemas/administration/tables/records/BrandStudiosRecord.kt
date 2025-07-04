/*
 * This file is generated by jOOQ.
 */
package by.anatolyloyko.ams.orm.jooq.schemas.administration.tables.records


import by.anatolyloyko.ams.orm.jooq.schemas.administration.tables.BrandStudios

import org.jooq.Field
import org.jooq.Record2
import org.jooq.Row2
import org.jooq.impl.TableRecordImpl


/**
 * This class is generated by jOOQ.
 */
@Suppress("UNCHECKED_CAST")
open class BrandStudiosRecord private constructor() : TableRecordImpl<BrandStudiosRecord>(BrandStudios.BRAND_STUDIOS), Record2<Long?, Long?> {

    open var brandId: Long?
        set(value): Unit = set(0, value)
        get(): Long? = get(0) as Long?

    open var studioId: Long?
        set(value): Unit = set(1, value)
        get(): Long? = get(1) as Long?

    // -------------------------------------------------------------------------
    // Record2 type implementation
    // -------------------------------------------------------------------------

    override fun fieldsRow(): Row2<Long?, Long?> = super.fieldsRow() as Row2<Long?, Long?>
    override fun valuesRow(): Row2<Long?, Long?> = super.valuesRow() as Row2<Long?, Long?>
    override fun field1(): Field<Long?> = BrandStudios.BRAND_STUDIOS.BRAND_ID
    override fun field2(): Field<Long?> = BrandStudios.BRAND_STUDIOS.STUDIO_ID
    override fun component1(): Long? = brandId
    override fun component2(): Long? = studioId
    override fun value1(): Long? = brandId
    override fun value2(): Long? = studioId

    override fun value1(value: Long?): BrandStudiosRecord {
        set(0, value)
        return this
    }

    override fun value2(value: Long?): BrandStudiosRecord {
        set(1, value)
        return this
    }

    override fun values(value1: Long?, value2: Long?): BrandStudiosRecord {
        this.value1(value1)
        this.value2(value2)
        return this
    }

    /**
     * Create a detached, initialised BrandStudiosRecord
     */
    constructor(brandId: Long? = null, studioId: Long? = null): this() {
        this.brandId = brandId
        this.studioId = studioId
        resetChangedOnNotNull()
    }
}
