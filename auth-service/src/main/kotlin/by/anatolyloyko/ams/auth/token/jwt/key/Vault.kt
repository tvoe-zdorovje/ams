package by.anatolyloyko.ams.auth.token.jwt.key

import com.nimbusds.jose.jwk.RSAKey
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

/**
 * Utility class intended to encapsulate the storage of RSA Key.
 */
class Vault(
    private val size: Int
) {
    private val keys = ArrayDeque<RSAKey>()
    private val lock = ReentrantLock()

    /**
     * Generates a new key every {@property jwt.private-key.rotation.period} milliseconds.
     *
     * If the vault is full, removes the oldest key.
     */
    fun rotate(key: RSAKey) = lock.withLock {
        if (keys.size >= size) {
            keys.removeLast()
        }
        keys.addFirst(key)
    }

    /**
     * Returns the newest, actual RSA Key is currently used to sign JWTs.
     *
     * @return an actual {@link RSAKey}
     */
    fun get(): RSAKey = lock.withLock {
        keys.first()
    }

    /**
     * Returns all stored RSA Keys.
     *
     * @return {@link List} of all stored {@link RSAKey}
     */
    fun getAll(): List<RSAKey> = keys.toList()
}
