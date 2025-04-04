package by.anatolyloyko.ams.orm.jooq.util

import org.jooq.DSLContext
import org.jooq.Query

fun DSLContext.executeBatch(vararg queries: Query) = batch(*queries).execute()
