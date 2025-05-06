package by.anatolyloyko.ams.common.infrastructure.service.command

import org.assertj.core.api.WithAssertions
import org.junit.jupiter.api.Test

class BaseCommandTest : WithAssertions {
    private val input = 123

    private val command = TestCommand(input)

    @Test
    fun `toString - must return correct string`() {
        assertThat(command.toString())
            .isEqualTo("${command.javaClass.simpleName}(input=$input)")
    }

    @Test
    fun `getInput - must return input`() {
        assertThat(command.input).isEqualTo(input)
    }

    private class TestCommand(input: Int) : BaseCommand<Int, String>(input)
}
