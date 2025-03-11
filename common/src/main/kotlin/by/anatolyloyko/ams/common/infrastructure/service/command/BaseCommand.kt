package by.anatolyloyko.ams.common.infrastructure.service.command

/**
 * A base class for commands that require input data.
 *
 * @param I the type of input required to execute the command.
 * @param R the type of command execution result.
 * @property input the input data required for execution.
 */
abstract class BaseCommand<I, R>(
    val input: I
) : Command<R> {
    override fun toString(): String = "${javaClass.simpleName}(input=$input)"
}
