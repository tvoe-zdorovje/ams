package by.anatolyloyko.ams.auth.token.jwt.key

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

// todo kdoc
@Component
class KeyManager(
    @Value("\${jwt.private-key.kid-format}")
    private val kidFormat: String,
    @Value("\${jwt.private-key.rotation.vault-size}")
    private val vaultSize: Int,
) {
    private val log = LoggerFactory.getLogger(KeyManager::class.java)

    private val generator: KeyGenerator = KeyGenerator(kidFormat)

    private val vault: Vault = Vault(vaultSize)

    fun get() = vault.get()

    @Scheduled(fixedDelayString = "\${jwt.private-key.rotation.period}")
    fun rotateKeys() {
        log.info("Scheduled key rotation")
        vault.rotate(generator.generate())
    }
}
