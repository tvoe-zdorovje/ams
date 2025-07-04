/*
 * This file is generated by jOOQ.
 */
package by.anatolyloyko.ams.orm.jooq.schemas.administration.tables


import by.anatolyloyko.ams.orm.jooq.schemas.administration.Administration
import by.anatolyloyko.ams.orm.jooq.schemas.administration.tables.records.PermissionRecord

import java.util.function.Function

import org.jooq.Condition
import org.jooq.Field
import org.jooq.ForeignKey
import org.jooq.InverseForeignKey
import org.jooq.Name
import org.jooq.Record
import org.jooq.Records
import org.jooq.Row3
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
open class Permission(
    alias: Name,
    path: Table<out Record>?,
    childPath: ForeignKey<out Record, PermissionRecord>?,
    parentPath: InverseForeignKey<out Record, PermissionRecord>?,
    aliased: Table<PermissionRecord>?,
    parameters: Array<Field<*>?>?,
    where: Condition?
): TableImpl<PermissionRecord>(
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
         * The reference instance of <code>administration.permission</code>
         */
        val PERMISSION: Permission = Permission()
    }

    /**
     * The class holding records for this type
     */
    override fun getRecordType(): Class<PermissionRecord> = PermissionRecord::class.java

    /**
     * The column <code>administration.permission.id</code>.
     */
    val ID: TableField<PermissionRecord, Long?> = createField(DSL.name("id"), SQLDataType.BIGINT.nullable(false), this, "")

    /**
     * The column <code>administration.permission.name</code>.
     */
    val NAME: TableField<PermissionRecord, String?> = createField(DSL.name("name"), SQLDataType.VARCHAR(100).nullable(false), this, "")

    /**
     * The column <code>administration.permission.description</code>.
     */
    val DESCRIPTION: TableField<PermissionRecord, String?> = createField(DSL.name("description"), SQLDataType.VARCHAR(255).nullable(false), this, "")

    private constructor(alias: Name, aliased: Table<PermissionRecord>?): this(alias, null, null, null, aliased, null, null)
    private constructor(alias: Name, aliased: Table<PermissionRecord>?, parameters: Array<Field<*>?>?): this(alias, null, null, null, aliased, parameters, null)

    /**
     * Create an aliased <code>administration.permission</code> table reference
     */
    constructor(alias: String): this(DSL.name(alias))

    /**
     * Create an aliased <code>administration.permission</code> table reference
     */
    constructor(alias: Name): this(alias, null)

    /**
     * Create a <code>administration.permission</code> table reference
     */
    constructor(): this(DSL.name("permission"), null)
    override fun getSchema(): Schema? = if (aliased()) null else Administration.ADMINISTRATION
    override fun getPrimaryKey(): UniqueKey<PermissionRecord> = Internal.createUniqueKey(Permission.PERMISSION, DSL.name("permission_pkey"), arrayOf(Permission.PERMISSION.ID), true)

    // -------------------------------------------------------------------------
    // Row3 type methods
    // -------------------------------------------------------------------------
    override fun fieldsRow(): Row3<Long?, String?, String?> = super.fieldsRow() as Row3<Long?, String?, String?>

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Function)}.
     */
    fun <U> mapping(from: (Long?, String?, String?) -> U): SelectField<U> = convertFrom(Records.mapping(from))

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Class,
     * Function)}.
     */
    fun <U> mapping(toType: Class<U>, from: (Long?, String?, String?) -> U): SelectField<U> = convertFrom(toType, Records.mapping(from))
}
