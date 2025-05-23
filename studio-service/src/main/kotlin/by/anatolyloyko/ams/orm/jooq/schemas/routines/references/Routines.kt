/*
 * This file is generated by jOOQ.
 */
package by.anatolyloyko.ams.orm.jooq.schemas.routines.references


import by.anatolyloyko.ams.orm.jooq.schemas.routines.SaveStudio

import org.jooq.Configuration
import org.jooq.Field



/**
 * Call <code>studios.save_studio</code>
 */
fun saveStudio(
      configuration: Configuration
    , iId: Long?
    , iName: String?
    , iDescription: String?
): Long? {
    val f = SaveStudio()
    f.setIId(iId)
    f.setIName(iName)
    f.setIDescription(iDescription)

    f.execute(configuration)
    return f.returnValue
}

/**
 * Get <code>studios.save_studio</code> as a field.
 */
fun saveStudio(
      iId: Long?
    , iName: String?
    , iDescription: String?
): Field<Long?> {
    val f = SaveStudio()
    f.setIId(iId)
    f.setIName(iName)
    f.setIDescription(iDescription)

    return f.asField()
}

/**
 * Get <code>studios.save_studio</code> as a field.
 */
fun saveStudio(
      iId: Field<Long?>
    , iName: Field<String?>
    , iDescription: Field<String?>
): Field<Long?> {
    val f = SaveStudio()
    f.setIId(iId)
    f.setIName(iName)
    f.setIDescription(iDescription)

    return f.asField()
}
