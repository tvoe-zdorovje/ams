package by.anatolyloyko.ams.user.finder

import by.anatolyloyko.ams.orm.exposed.schemas.users.table.UserTable
import org.assertj.core.api.WithAssertions
import org.jetbrains.exposed.sql.Schema
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance
import org.springframework.boot.test.context.SpringBootTest

/**
 * Abstract base class for testing `Finder` implemented using the `Exposed` library.
 *
 * Before running tests, it sets up an in-memory database,
 * and creates the necessary database schemas and tables for testing purposes.
 *
 * Tests should extend this class to inherit the database setup.
 */
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
abstract class ExposedFinderTest : WithAssertions {
    @BeforeAll
    fun beforeAllTests() {
        setUpDatabase()
    }

    private fun setUpDatabase() = transaction {
        SchemaUtils.createSchema(Schema("users"))

        SchemaUtils.create(UserTable)
    }
}
