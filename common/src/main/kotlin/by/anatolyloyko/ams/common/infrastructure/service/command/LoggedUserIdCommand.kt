package by.anatolyloyko.ams.common.infrastructure.service.command

abstract class LoggedUserIdCommand<I, R>(
    input: I,
    val loggedUserId: Long,
) : BaseCommand<I, R>(input) {
    override fun toString(): String = "${javaClass.simpleName}(input=$input, loggedUserId=$loggedUserId)"
}
