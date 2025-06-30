package by.anatolyloyko.ams.auth.token.jwt.key

import com.nimbusds.jose.jwk.JWKSet
import com.nimbusds.jose.jwk.RSAKey
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

/**
 * Utility class responsible for RSA Keys management.
 *
 * Generates a new key every {@property jwt.private-key.rotation.period} milliseconds
 * and stores up to {@property jwt.private-key.rotation.vault-size} generated keys.
 *
 * Exposes JSON Web Key Sets (JWKS).
 *
 * @see Vault
 * @see KeyGenerator
 */
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

    /**
     * Returns an actual RSA Key is currently used to sign JWTs.
     *
     * @return an actual {@link RSAKey}
     * @see Vault
     */
    fun getKey() = vault.get()

    /**
     * Returns JSON Web Key Set (JWKS) of all stored RSA Keys.
     *
     * @see Vault
     */
    fun getJwkSet() = JWKSet(
        vault
            .getAll()
            .map(RSAKey::toPublicJWK)
    )

    /**
     * Generates a new key every {@property jwt.private-key.rotation.period} milliseconds.
     *
     * If the vault is full, removes the oldest key.
     *
     * @see Vault
     */
    @Scheduled(fixedDelayString = "\${jwt.private-key.rotation.period}")
    fun rotateKeys() {
        log.info("Scheduled key rotation")
        vault.rotate(generator.generate())
    }
}
