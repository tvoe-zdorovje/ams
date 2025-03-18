package by.anatolyloyko.ams.user.finder

import by.anatolyloyko.ams.orm.exposed.schemas.users.UserTable
import by.anatolyloyko.ams.user.USER
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class ExposedUserFinderTest @Autowired constructor(
    private val userFinder: UserFinder
) : ExposedFinderTest() {
    @BeforeAll
    fun beforeAll() {
        transaction {
            UserTable.insert {
                it[id] = USER.id!!
                it[firstName] = USER.firstName
                it[lastName] = USER.lastName
            }
        }
    }

    @Test
    fun `must find user by id`() {
        val expected = USER

        val actual = userFinder.findById(expected.id!!)

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `must return null if no user found`() {
        val actual = userFinder.findById(-1)

        assertThat(actual).isNull()
    }
}
