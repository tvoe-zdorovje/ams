package by.anatolyloyko.ams.user.action

import by.anatolyloyko.ams.orm.jooq.schemas.users.routines.references.saveUser
import by.anatolyloyko.ams.user.ROUTINES_REFERENCE
import by.anatolyloyko.ams.user.USER
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import org.assertj.core.api.WithAssertions
import org.jooq.DSLContext
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.lang.IllegalStateException

class JooqUpdateUserActionTest : WithAssertions {
    private val dslContext = mockk<DSLContext>(relaxed = true)

    private val action = JooqUpdateUserAction(dslContext)

    @Test
    fun `must call save user routine`() = mockkStatic(ROUTINES_REFERENCE) {
        val user = USER
        every { saveUser(any(), any(), any(), any(), any()) } returns user.id!!

        action(user)

        verify(exactly = 1) {
            saveUser(
                configuration = any(),
                iId = user.id,
                iFirstName = user.firstName,
                iLastName = user.lastName,
                iPhoneNumber = user.phoneNumber
            )
        }
    }

    @Test
    fun `must throw exception when result is null`() = mockkStatic(ROUTINES_REFERENCE) {
        val user = USER
        every { saveUser(any(), any(), any(), any(), any()) } returns null

        assertThrows<IllegalStateException> {
            action(user)
        }.also {
            assertThat(it.message).isEqualTo("Could not save user $user")
        }
    }
}
