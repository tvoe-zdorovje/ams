/*
 * This file is generated by jOOQ.
 */
package by.anatolyloyko.ams.orm.jooq.schemas.administration.tables


import by.anatolyloyko.ams.orm.jooq.schemas.administration.Administration
import by.anatolyloyko.ams.orm.jooq.schemas.administration.tables.records.RolePermissionsRecord

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
open class RolePermissions(
    alias: Name,
    path: Table<out Record>?,
    childPath: ForeignKey<out Record, RolePermissionsRecord>?,
    parentPath: InverseForeignKey<out Record, RolePermissionsRecord>?,
    aliased: Table<RolePermissionsRecord>?,
    parameters: Array<Field<*>?>?,
    where: Condition?
): TableImpl<RolePermissionsRecord>(
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
         * The reference instance of
         * <code>administration.role_permissions</code>
         */
        val ROLE_PERMISSIONS: RolePermissions = RolePermissions()
    }

    /**
     * The class holding records for this type
     */
    override fun getRecordType(): Class<RolePermissionsRecord> = RolePermissionsRecord::class.java

    /**
     * The column <code>administration.role_permissions.role_id</code>.
     */
    val ROLE_ID: TableField<RolePermissionsRecord, Long?> = createField(DSL.name("role_id"), SQLDataType.BIGINT, this, "")

    /**
     * The column <code>administration.role_permissions.permission_id</code>.
     */
    val PERMISSION_ID: TableField<RolePermissionsRecord, Long?> = createField(DSL.name("permission_id"), SQLDataType.BIGINT, this, "")

    private constructor(alias: Name, aliased: Table<RolePermissionsRecord>?): this(alias, null, null, null, aliased, null, null)
    private constructor(alias: Name, aliased: Table<RolePermissionsRecord>?, parameters: Array<Field<*>?>?): this(alias, null, null, null, aliased, parameters, null)

    /**
     * Create an aliased <code>administration.role_permissions</code> table
     * reference
     */
    constructor(alias: String): this(DSL.name(alias))

    /**
     * Create an aliased <code>administration.role_permissions</code> table
     * reference
     */
    constructor(alias: Name): this(alias, null)

    /**
     * Create a <code>administration.role_permissions</code> table reference
     */
    constructor(): this(DSL.name("role_permissions"), null)
    override fun getSchema(): Schema? = if (aliased()) null else Administration.ADMINISTRATION
    override fun getUniqueKeys(): List<UniqueKey<RolePermissionsRecord>> = listOf(
        Internal.createUniqueKey(RolePermissions.ROLE_PERMISSIONS, DSL.name("unique_role_id_permission_id"), arrayOf(RolePermissions.ROLE_PERMISSIONS.ROLE_ID, RolePermissions.ROLE_PERMISSIONS.PERMISSION_ID), true)
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
