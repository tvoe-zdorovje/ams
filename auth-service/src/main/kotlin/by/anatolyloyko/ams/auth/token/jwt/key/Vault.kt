package by.anatolyloyko.ams.auth.token.jwt.key

import com.nimbusds.jose.jwk.RSAKey
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock


class Vault(
    private val size: Int
) {
    private val keys = ArrayDeque<RSAKey>()
    private val lock = ReentrantLock()

    // todo kdoc
    fun rotate(key: RSAKey) = lock.withLock {
        if (keys.size >= size) {
            keys.removeLast()
        }
        keys.addFirst(key)
    }

    fun get(): RSAKey = lock.withLock {
        keys.first()
    }
}
