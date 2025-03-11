package by.anatolyloyko.ams.infrastructure.kotlin

/**
 * Executes the given [block] on the receiver if the specified [condition] is met and returns the receiver.
 *
 * Generally, this function extends the standard `also` by adding a conditional execution.
 * If the [condition] is `true`, the [block] is invoked with the receiver as its argument.
 * If the [condition] is `false`, the receiver is returned unchanged.
 *
 * @param condition the condition that determines whether the [block] should be executed.
 * @param block the block of code to execute if the condition is met.
 * @return the original receiver object.
 */
internal inline fun <T> T.alsoIf(condition: Boolean, block: (T) -> Unit): T {
    if (condition) {
        block(this)
    }

    return this
}

/**
 * Converts the first character of the string to uppercase.
 *
 * @return a new string with the first character in uppercase.
 */
internal fun String.uppercaseFirstChar() = replaceFirstChar {
    if (it.isLowerCase()) it.uppercaseChar() else it
}

/**
 * Converts the first character of the string to lowercase.
 *
 * @return a new string with the first character in lowercase.
 */
internal fun String.lowercaseFirstChar() = replaceFirstChar {
    if (it.isUpperCase()) it.lowercaseChar() else it
}
