package by.anatolyloyko.ams.infrastructure.service.command

import org.assertj.core.api.WithAssertions
import org.junit.jupiter.api.Test

class BaseCommandHandlerTest : WithAssertions {
    private val commandHandler = object : BaseCommandHandler<TestCommand, Unit>() {
        override fun handleInternal(command: TestCommand) = TODO("Not yet implemented")
    }

    @Test
    fun `canHandle - must return true`() {
        assertThat(commandHandler.canHandle(TestCommand()))
    }

    @Test
    fun `canHandle - must return false`() {
        val command = object : Command<Unit> {}

        assertThat(commandHandler.canHandle(command)).isFalse()
    }

    class TestCommand : Command<Unit>
}
