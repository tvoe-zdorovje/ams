package by.anatolyloyko.ams.brand.database

import by.anatolyloyko.ams.brand.model.Brand
import org.springframework.stereotype.Repository
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicLong

/**
 * Temporary dummy implementation. Mocks DB
 */
@Repository
class DummyBrandRepository {
    private val idGenerator = AtomicLong(0)
    private val brandTable = ConcurrentHashMap<Long, Brand>()

    fun save(brand: Brand): Long {
        val brandWithId = if (brand.id == null) {
            brand.copy(id = idGenerator.incrementAndGet())
        } else {
            brand
        }

        brandTable[brandWithId.id!!] = brandWithId

        return brandWithId.id
    }

    fun findById(id: Long): Brand? = brandTable[id]
}
