package by.anatolyloyko.ams.auth.token.jwt.action

import by.anatolyloyko.ams.auth.token.action.GenerateTokenAction
import by.anatolyloyko.ams.auth.token.jwt.key.KeyManager
import by.anatolyloyko.ams.auth.token.model.TokenData
import com.nimbusds.jose.JOSEObjectType
import com.nimbusds.jose.JWSAlgorithm
import com.nimbusds.jose.JWSHeader
import com.nimbusds.jose.crypto.RSASSASigner
import com.nimbusds.jwt.JWTClaimsSet
import com.nimbusds.jwt.SignedJWT
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.Date
import java.util.UUID

private const val CLAIM_DATA = "data"

/**
 * {@inheritDoc}
 *
 * This implementation is intended to generate JWT.
 */
@Component
class JwtGenerateTokenAction(
    private val keyManager: KeyManager,
    @Value("\${jwt.time-of-life}")
    private val timeOfLife: Long,
    @Value("\${jwt.algorithm}")
    private val algorithm: String,
) : GenerateTokenAction {
    override fun invoke(tokenData: TokenData): String {
        val claims = JWTClaimsSet
            .Builder()
            .issueTime(Date())
            .expirationTime(Date(System.currentTimeMillis() + timeOfLife))
            .jwtID("${tokenData.userId}-${UUID.randomUUID()}")
            .claim(CLAIM_DATA, tokenData)
            .build()

        val rsaKey = keyManager.getKey()
        val header = JWSHeader.Builder(JWSAlgorithm.parse(algorithm))
            .keyID(rsaKey.keyID)
            .type(JOSEObjectType.JWT)
            .build()

        return with(SignedJWT(header, claims)) {
            sign(RSASSASigner(rsaKey.toPrivateKey()))
            serialize()
        }
    }
}
