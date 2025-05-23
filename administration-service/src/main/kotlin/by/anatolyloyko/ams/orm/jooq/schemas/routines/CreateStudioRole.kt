/*
 * This file is generated by jOOQ.
 */
package by.anatolyloyko.ams.orm.jooq.schemas.routines


import by.anatolyloyko.ams.orm.jooq.schemas.Administration

import org.jooq.Field
import org.jooq.Parameter
import org.jooq.impl.AbstractRoutine
import org.jooq.impl.Internal
import org.jooq.impl.SQLDataType


/**
 * This class is generated by jOOQ.
 */
@Suppress("UNCHECKED_CAST")
open class CreateStudioRole : AbstractRoutine<Long>("create_studio_role", Administration.ADMINISTRATION, SQLDataType.BIGINT) {
    companion object {

        /**
         * The parameter
         * <code>administration.create_studio_role.RETURN_VALUE</code>.
         */
        val RETURN_VALUE: Parameter<Long?> = Internal.createParameter("RETURN_VALUE", SQLDataType.BIGINT, false, false)

        /**
         * The parameter
         * <code>administration.create_studio_role.i_studio_id</code>.
         */
        val I_STUDIO_ID: Parameter<Long?> = Internal.createParameter("i_studio_id", SQLDataType.BIGINT, false, false)

        /**
         * The parameter <code>administration.create_studio_role.i_name</code>.
         */
        val I_NAME: Parameter<String?> = Internal.createParameter("i_name", SQLDataType.VARCHAR, false, false)

        /**
         * The parameter
         * <code>administration.create_studio_role.i_description</code>.
         */
        val I_DESCRIPTION: Parameter<String?> = Internal.createParameter("i_description", SQLDataType.VARCHAR, false, false)

        /**
         * The parameter
         * <code>administration.create_studio_role.i_permissions</code>.
         */
        val I_PERMISSIONS: Parameter<Array<Long?>?> = Internal.createParameter("i_permissions", SQLDataType.BIGINT.array(), false, false)
    }

    init {
        returnParameter = CreateStudioRole.RETURN_VALUE
        addInParameter(CreateStudioRole.I_STUDIO_ID)
        addInParameter(CreateStudioRole.I_NAME)
        addInParameter(CreateStudioRole.I_DESCRIPTION)
        addInParameter(CreateStudioRole.I_PERMISSIONS)
    }

    /**
     * Set the <code>i_studio_id</code> parameter IN value to the routine
     */
    fun setIStudioId(value: Long?): Unit = setValue(CreateStudioRole.I_STUDIO_ID, value)

    /**
     * Set the <code>i_studio_id</code> parameter to the function to be used
     * with a {@link org.jooq.Select} statement
     */
    fun setIStudioId(field: Field<Long?>): Unit {
        setField(CreateStudioRole.I_STUDIO_ID, field)
    }

    /**
     * Set the <code>i_name</code> parameter IN value to the routine
     */
    fun setIName(value: String?): Unit = setValue(CreateStudioRole.I_NAME, value)

    /**
     * Set the <code>i_name</code> parameter to the function to be used with a
     * {@link org.jooq.Select} statement
     */
    fun setIName(field: Field<String?>): Unit {
        setField(CreateStudioRole.I_NAME, field)
    }

    /**
     * Set the <code>i_description</code> parameter IN value to the routine
     */
    fun setIDescription(value: String?): Unit = setValue(CreateStudioRole.I_DESCRIPTION, value)

    /**
     * Set the <code>i_description</code> parameter to the function to be used
     * with a {@link org.jooq.Select} statement
     */
    fun setIDescription(field: Field<String?>): Unit {
        setField(CreateStudioRole.I_DESCRIPTION, field)
    }

    /**
     * Set the <code>i_permissions</code> parameter IN value to the routine
     */
    fun setIPermissions(value: Array<Long?>?): Unit = setValue(CreateStudioRole.I_PERMISSIONS, value)

    /**
     * Set the <code>i_permissions</code> parameter to the function to be used
     * with a {@link org.jooq.Select} statement
     */
    fun setIPermissions(field: Field<Array<Long?>?>): Unit {
        setField(CreateStudioRole.I_PERMISSIONS, field)
    }
}
