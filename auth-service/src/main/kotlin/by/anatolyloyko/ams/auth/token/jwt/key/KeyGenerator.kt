package by.anatolyloyko.ams.auth.token.jwt.key

import com.nimbusds.jose.jwk.RSAKey
import com.nimbusds.jose.jwk.gen.RSAKeyGenerator

/**
 * Utility class intended to encapsulate RSA Key generation.
 */
class KeyGenerator(
    private val kidFormat: String,
    keySize: Int = RSAKeyGenerator.MIN_KEY_SIZE_BITS,
) {
    private val generator: RSAKeyGenerator = RSAKeyGenerator(keySize)

    /**
     * Generates RSA Key with ID matching to the {@property kidFormat}.
     *
     * @return generated {@link RSAKey}
     */
    fun generate(): RSAKey = generator
        .keyID(kidFormat.format(System.currentTimeMillis()))
        .generate()
}
