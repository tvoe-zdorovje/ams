/*
 * This file is generated by jOOQ.
 */
package by.anatolyloyko.ams.orm.jooq.schemas.administration.tables


import by.anatolyloyko.ams.orm.jooq.schemas.administration.Administration
import by.anatolyloyko.ams.orm.jooq.schemas.administration.tables.records.StudioRolesRecord

import java.util.function.Function

import kotlin.collections.List

import org.jooq.Condition
import org.jooq.Field
import org.jooq.ForeignKey
import org.jooq.InverseForeignKey
import org.jooq.Name
import org.jooq.Record
import org.jooq.Records
import org.jooq.Row2
import org.jooq.Schema
import org.jooq.SelectField
import org.jooq.Table
import org.jooq.TableField
import org.jooq.TableOptions
import org.jooq.UniqueKey
import org.jooq.impl.DSL
import org.jooq.impl.Internal
import org.jooq.impl.SQLDataType
import org.jooq.impl.TableImpl


/**
 * This class is generated by jOOQ.
 */
@Suppress("UNCHECKED_CAST")
open class StudioRoles(
    alias: Name,
    path: Table<out Record>?,
    childPath: ForeignKey<out Record, StudioRolesRecord>?,
    parentPath: InverseForeignKey<out Record, StudioRolesRecord>?,
    aliased: Table<StudioRolesRecord>?,
    parameters: Array<Field<*>?>?,
    where: Condition?
): TableImpl<StudioRolesRecord>(
    alias,
    Administration.ADMINISTRATION,
    path,
    childPath,
    parentPath,
    aliased,
    parameters,
    DSL.comment(""),
    TableOptions.table(),
    where,
) {
    companion object {

        /**
         * The reference instance of <code>administration.studio_roles</code>
         */
        val STUDIO_ROLES: StudioRoles = StudioRoles()
    }

    /**
     * The class holding records for this type
     */
    override fun getRecordType(): Class<StudioRolesRecord> = StudioRolesRecord::class.java

    /**
     * The column <code>administration.studio_roles.studio_id</code>.
     */
    val STUDIO_ID: TableField<StudioRolesRecord, Long?> = createField(DSL.name("studio_id"), SQLDataType.BIGINT, this, "")

    /**
     * The column <code>administration.studio_roles.role_id</code>.
     */
    val ROLE_ID: TableField<StudioRolesRecord, Long?> = createField(DSL.name("role_id"), SQLDataType.BIGINT, this, "")

    private constructor(alias: Name, aliased: Table<StudioRolesRecord>?): this(alias, null, null, null, aliased, null, null)
    private constructor(alias: Name, aliased: Table<StudioRolesRecord>?, parameters: Array<Field<*>?>?): this(alias, null, null, null, aliased, parameters, null)

    /**
     * Create an aliased <code>administration.studio_roles</code> table
     * reference
     */
    constructor(alias: String): this(DSL.name(alias))

    /**
     * Create an aliased <code>administration.studio_roles</code> table
     * reference
     */
    constructor(alias: Name): this(alias, null)

    /**
     * Create a <code>administration.studio_roles</code> table reference
     */
    constructor(): this(DSL.name("studio_roles"), null)
    override fun getSchema(): Schema? = if (aliased()) null else Administration.ADMINISTRATION
    override fun getUniqueKeys(): List<UniqueKey<StudioRolesRecord>> = listOf(
        Internal.createUniqueKey(StudioRoles.STUDIO_ROLES, DSL.name("unique_studio_id_role_id"), arrayOf(StudioRoles.STUDIO_ROLES.STUDIO_ID, StudioRoles.STUDIO_ROLES.ROLE_ID), true)
    )

    // -------------------------------------------------------------------------
    // Row2 type methods
    // -------------------------------------------------------------------------
    override fun fieldsRow(): Row2<Long?, Long?> = super.fieldsRow() as Row2<Long?, Long?>

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Function)}.
     */
    fun <U> mapping(from: (Long?, Long?) -> U): SelectField<U> = convertFrom(Records.mapping(from))

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Class,
     * Function)}.
     */
    fun <U> mapping(toType: Class<U>, from: (Long?, Long?) -> U): SelectField<U> = convertFrom(toType, Records.mapping(from))
}
