package by.anatolyloyko.ams.user.action

import by.anatolyloyko.ams.orm.exposed.schemas.users.table.UserTable
import by.anatolyloyko.ams.user.ExposedTest
import by.anatolyloyko.ams.user.NEW_USER
import by.anatolyloyko.ams.user.USER
import by.anatolyloyko.ams.user.model.User
import io.mockk.every
import io.mockk.mockkObject
import org.jetbrains.exposed.sql.selectAll
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.transaction.annotation.Transactional
import kotlin.random.Random

internal class ExposedCreateUserActionTest @Autowired constructor(
    private val createUserAction: ExposedCreateUserAction,
) : ExposedTest() {
    @Test
    @Transactional
    fun `must create a new user`() {
        mockkObject(Random.Default) {
            val expectedUserId = USER.id!!
            every { Random.Default.nextLong() } returns expectedUserId

            val actualUserId = createUserAction(NEW_USER)

            assertThat(actualUserId).isEqualTo(expectedUserId)

            val actualUser = UserTable
                .selectAll()
                .where { UserTable.id eq expectedUserId }
                .map {
                    User(
                        id = it[UserTable.id],
                        firstName = it[UserTable.firstName],
                        lastName = it[UserTable.lastName],
                    )
                }
                .singleOrNull()
                ?: fail()

            assertThat(actualUser).isEqualTo(USER)
        }
    }
}
