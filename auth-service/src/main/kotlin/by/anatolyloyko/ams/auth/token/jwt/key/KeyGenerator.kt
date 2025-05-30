package by.anatolyloyko.ams.auth.token.jwt.key

import com.nimbusds.jose.jwk.RSAKey
import com.nimbusds.jose.jwk.gen.RSAKeyGenerator

// todo kdoc
class KeyGenerator(
    private val kidFormat: String,
    keySize: Int = RSAKeyGenerator.MIN_KEY_SIZE_BITS,
) {
    private val generator: RSAKeyGenerator = RSAKeyGenerator(keySize)

    fun generate(): RSAKey = generator
        .keyID(kidFormat.format(System.currentTimeMillis()))
        .generate()
}
