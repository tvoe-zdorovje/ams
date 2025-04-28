package by.anatolyloyko.ams.administration

import by.anatolyloyko.ams.orm.jooq.schemas.Administration
import by.anatolyloyko.ams.orm.jooq.schemas.tables.references.PERMISSION
import by.anatolyloyko.ams.orm.jooq.schemas.tables.references.ROLE
import by.anatolyloyko.ams.orm.jooq.schemas.tables.references.ROLE_PERMISSIONS
import by.anatolyloyko.ams.orm.jooq.util.executeBatch
import org.assertj.core.api.WithAssertions
import org.jooq.DSLContext
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional

/**
 * Abstract base class for testing `Finder` and `Action` implemented using the `jOOQ` library.
 *
 * Before running tests, it sets up an in-memory database,
 * and creates the necessary database schemas, tables and functions for testing purposes.
 *
 * Tests should extend this class to inherit the database setup.
 */
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
abstract class JooqTest : WithAssertions {
    @Autowired
    lateinit var dslContext: DSLContext

    @BeforeAll
    fun beforeAllTests() {
        setUpDatabase()
    }

    private fun setUpDatabase() = with(dslContext) {
        executeBatch(
            createSchemaIfNotExists(Administration()),

            createTableIfNotExists(ROLE)
                .columns(*ROLE.fields()),

            createTableIfNotExists(PERMISSION)
                .columns(*PERMISSION.fields()),

            createTableIfNotExists(ROLE_PERMISSIONS)
                .columns(*ROLE_PERMISSIONS.fields())
        )
    }
}
