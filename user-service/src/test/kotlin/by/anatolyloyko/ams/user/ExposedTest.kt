package by.anatolyloyko.ams.user

import by.anatolyloyko.ams.orm.exposed.schemas.users.table.UserTable
import org.assertj.core.api.WithAssertions
import org.h2.jdbc.JdbcSQLIntegrityConstraintViolationException
import org.h2.tools.SimpleResultSet
import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.jetbrains.exposed.sql.ColumnType
import org.jetbrains.exposed.sql.LongColumnType
import org.jetbrains.exposed.sql.Schema
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance
import org.springframework.boot.test.context.SpringBootTest
import java.sql.ResultSet
import kotlin.random.Random

/**
 * Abstract base class for testing `Finder` and `Action` implemented using the `Exposed` library.
 *
 * Before running tests, it sets up an in-memory database,
 * and creates the necessary database schemas, tables and functions for testing purposes.
 *
 * Tests should extend this class to inherit the database setup.
 */
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal abstract class ExposedTest : WithAssertions {
    @BeforeAll
    fun beforeAllTests() {
        setUpDatabase()
    }

    private fun setUpDatabase() = transaction {
        SchemaUtils.createSchema(Schema("users"))

        SchemaUtils.create(UserTable)

        exec("CREATE ALIAS IF NOT EXISTS users.save_user FOR 'by.anatolyloyko.ams.user.ExposedTest.insertUser'")
    }

    companion object {
        @JvmStatic
        fun insertUser(id: Long?, firstName: String, lastName: String): ResultSet {
            val userId = id ?: Random.nextLong()

            return function(
                LongColumnType(),
                userId,
                id == null
            ) {
                UserTable.insert {
                    it[UserTable.id] = userId
                    it[UserTable.firstName] = firstName
                    it[UserTable.lastName] = lastName
                }
            }
        }

        private fun <T> function(
            resultType: ColumnType<T>,
            result: T,
            // h2 executes functions multiple times, so the exception should be ignored when creating a new record
            ignoreIntegrityConstraintViolation: Boolean = true,
            script: () -> Unit
        ): ResultSet = try {
            script()
        } catch (e: ExposedSQLException) {
            val isIntegrityConstraintViolation = JdbcSQLIntegrityConstraintViolationException::class.isInstance(e.cause)
            if (ignoreIntegrityConstraintViolation && isIntegrityConstraintViolation) Unit else throw e
        }.let {
            SimpleResultSet().apply {
                addColumn("col1", 0, resultType.sqlType(), 8, 0)
                addRow(result)
            }
        }
    }
}
