package by.anatolyloyko.ams.infrastructure.logging

import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Extension property to provide a logger for any class.
 *
 * This property allows accessing a logger for the class without the need
 * to explicitly create a logger instance. The logger is created using the
 * class of the object (`T::class.java`).
 *
 * @param T the type of the object for which the logger is created.
 * @return a logger instance for the type `T`.
 */
val <reified T> T.log: Logger inline get() = LoggerFactory.getLogger(T::class.java)
