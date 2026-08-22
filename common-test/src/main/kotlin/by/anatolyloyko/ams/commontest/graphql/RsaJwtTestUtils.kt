package by.anatolyloyko.ams.commontest.graphql

import com.nimbusds.jose.JWSAlgorithm
import com.nimbusds.jose.JWSHeader
import com.nimbusds.jose.crypto.RSASSASigner
import com.nimbusds.jwt.JWTClaimsSet
import com.nimbusds.jwt.SignedJWT
import java.security.KeyPairGenerator
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey

object RsaJwtTestUtils {
    private val keyPair = KeyPairGenerator
        .getInstance("RSA")
        .also { it.initialize(2048) }
        .generateKeyPair()

    private val signer = RSASSASigner(keyPair.private)

    val PUBLIC_KEY: RSAPublicKey = keyPair.public as RSAPublicKey

    val PRIVATE_KEY: RSAPrivateKey = keyPair.private as RSAPrivateKey

    fun generateJwt(sub: String, claims: Map<String, Any>): String = SignedJWT(
        JWSHeader(JWSAlgorithm.RS256),
        JWTClaimsSet.Builder()
            .subject(sub)
            .also { claims.forEach(it::claim) }
            .build()
    )
        .also { it.sign(signer) }
        .serialize()
}