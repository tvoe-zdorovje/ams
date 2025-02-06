package by.anatolyloyko.ams.studio.database

import by.anatolyloyko.ams.studio.model.Studio
import org.springframework.stereotype.Repository
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicLong

/**
 * Temporary dummy implementation. Mocks DB
 */
@Repository
class DummyStudioRepository {
    private val idGenerator = AtomicLong(0)
    private val studioTable = ConcurrentHashMap<Long, Studio>()

    fun save(studio: Studio): Long {
        val studioWithId = if (studio.id == null) {
            studio.copy(id = idGenerator.incrementAndGet())
        } else {
            studio
        }

        studioTable[studioWithId.id!!] = studioWithId

        return studioWithId.id
    }

    fun findById(id: Long): Studio? = studioTable[id]
}
