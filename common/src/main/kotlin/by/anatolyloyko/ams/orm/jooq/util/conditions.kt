package by.anatolyloyko.ams.orm.jooq.util

import org.jooq.Condition
import org.jooq.Field

infix fun <T> Field<T>.eq(other: T): Condition = this.eq(other)

infix fun <T> Field<T>.eq(other: Field<T>): Condition = this.eq(other)
