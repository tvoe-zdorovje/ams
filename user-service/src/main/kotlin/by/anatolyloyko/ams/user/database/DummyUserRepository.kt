package by.anatolyloyko.ams.user.database

import by.anatolyloyko.ams.user.model.User
import org.springframework.stereotype.Repository
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicLong

/**
 * Temporary dummy implementation. Mocks DB
 */
@Repository
class DummyUserRepository {
    private val idGenerator = AtomicLong(0)
    private val userTable = ConcurrentHashMap<Long, User>()

    fun save(user: User): Long {
        val userWithId = if (user.id == null) {
            user.copy(id = idGenerator.incrementAndGet())
        } else {
            user
        }

        userTable[userWithId.id!!] = userWithId

        return userWithId.id
    }

    fun findById(id: Long): User? = userTable[id]
}
