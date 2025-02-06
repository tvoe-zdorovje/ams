package by.anatolyloyko.ams.user.finder

import by.anatolyloyko.ams.user.database.DummyUserRepository
import by.anatolyloyko.ams.user.model.User
import org.springframework.stereotype.Component

/**
 * Temporary dummy implementation
 */
@Component
class DummyUserFinder(
    private val dummyUserRepository: DummyUserRepository
) : UserFinder {
    override fun findById(id: Long): User? = dummyUserRepository.findById(id)
}
