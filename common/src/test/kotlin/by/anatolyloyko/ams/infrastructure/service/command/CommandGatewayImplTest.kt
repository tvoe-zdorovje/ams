package by.anatolyloyko.ams.infrastructure.service.command

import org.assertj.core.api.WithAssertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(
    classes = [
        CommandGatewayImpl::class,
        CommandGatewayImplTest.TestCommand1CommandHandler::class,
        CommandGatewayImplTest.TestCommand2CommandHandler::class
    ]
)
class CommandGatewayImplTest : WithAssertions {
    @Autowired
    lateinit var gateway: CommandGatewayImpl

    @Test
    fun `must handle commands`() {
        val commandToHandlerMap = mapOf(
            TestCommand1() to TestCommand1CommandHandler(),
            TestCommand2() to TestCommand2CommandHandler()
        )

        val result = commandToHandlerMap.map { gateway.handle(it.key) }

        val expected = commandToHandlerMap.map { it.value.handle(it.key) }

        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `must fail when no handlers found`() {
        val command = TestCommand3()
        assertThatThrownBy { gateway.handle(command) }
            .isInstanceOf(IllegalStateException::class.java)
            .hasMessageContaining("Cannot find command handler for $command")
            .rootCause()
            .isInstanceOf(NoSuchElementException::class.java)
            .hasMessage("Collection contains no element matching the predicate.")
    }

    @Test
    fun `must fail when multiple handlers found`() {
        val commandGateway = CommandGatewayImpl(
            commandHandlers = List(2) { TestCommand1CommandHandler() },
        )

        val command = TestCommand1()
        assertThatThrownBy { commandGateway.handle(command) }
            .isInstanceOf(IllegalStateException::class.java)
            .hasMessageContaining("Cannot find command handler for $command")
            .isInstanceOf(IllegalStateException::class.java)
            .hasMessageStartingWith("Cannot find command handler for")
    }

    class TestCommand1 : Command<String>

    class TestCommand2 : Command<String>

    class TestCommand3 : Command<String>

    class TestCommand1CommandHandler : BaseCommandHandler<TestCommand1, String>() {
        override fun handleInternal(command: TestCommand1): String =
            "${command.javaClass.simpleName} handled by ${this.javaClass.simpleName}"
    }

    class TestCommand2CommandHandler : BaseCommandHandler<TestCommand2, String>() {
        override fun handleInternal(command: TestCommand2): String =
            "${command.javaClass.simpleName} handled by ${this.javaClass.simpleName}"
    }
}
