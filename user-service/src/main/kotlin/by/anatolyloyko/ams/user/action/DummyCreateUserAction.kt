package by.anatolyloyko.ams.user.action

import by.anatolyloyko.ams.user.database.DummyUserRepository
import by.anatolyloyko.ams.user.model.User
import org.springframework.stereotype.Component

/**
 * Temporary dummy implementation.
 */
@Component
class DummyCreateUserAction(
    private val dummyUserRepository: DummyUserRepository
) : CreateUserAction {
    override fun invoke(user: User): Long = dummyUserRepository.save(user)
}
