package by.anatolyloyko.ams.auth.token.jwt.action

import by.anatolyloyko.ams.auth.token.jwt.key.KeyManager
import by.anatolyloyko.ams.auth.token.model.Permission
import by.anatolyloyko.ams.auth.token.model.TokenData
import com.nimbusds.jose.crypto.RSASSAVerifier
import com.nimbusds.jose.jwk.gen.RSAKeyGenerator
import com.nimbusds.jwt.SignedJWT
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.WithAssertions
import org.junit.jupiter.api.Test
import java.util.Date

private const val TIME_OF_LIFE = 300000L

private const val ALGORITHM = "RS256"

class JwtGenerateTokenActionTest : WithAssertions {
    private val rsaKey = RSAKeyGenerator(RSAKeyGenerator.MIN_KEY_SIZE_BITS)
        .keyID("key-id")
        .generate()

    private val keyManager = mockk<KeyManager>() {
        every { getKey() } returns rsaKey
    }

    private val jwtGenerateTokenAction = JwtGenerateTokenAction(keyManager, TIME_OF_LIFE, ALGORITHM)

    @Test
    fun `must generate valid JWT`() {
        val expectedTokenData = TokenData(
            userId = 123L,
            permissions = mapOf(
                10L to listOf(
                    Permission(11L, "perm11"),
                    Permission(12L, "perm12")
                ),
                20L to listOf(
                    Permission(21L, "perm21"),
                    Permission(22L, "perm22")
                )
            )
        )

        val result = jwtGenerateTokenAction(expectedTokenData)

        val verifier = RSASSAVerifier(rsaKey)
        val signedJwt = SignedJWT.parse(result)
        val claims = signedJwt.jwtClaimsSet

        assertThat(signedJwt.verify(verifier)).isTrue
        assertThat(claims.expirationTime).isEqualTo(Date(claims.issueTime.time + TIME_OF_LIFE))
        val uuidRegex = "\\w{8}(?:-\\w{4}){3}-\\w{12}"
        assertThat(claims.jwtid).matches("^${rsaKey.keyID}-${expectedTokenData.userId}-$uuidRegex$")
        val actualTokenData = claims.getJSONObjectClaim("data")
        assertThat(actualTokenData["userId"]).isEqualTo(expectedTokenData.userId)
        val actualTokenDataPermissions = actualTokenData["permissions"] as Map<String, Any>
        expectedTokenData.permissions.forEach { (entityId, permissions) ->
            val actualTokenDataPermissionList = actualTokenDataPermissions["$entityId"] as List<Map<String, Any>>
            permissions.forEachIndexed { index, permission ->
                val actualTokenDataPermission = actualTokenDataPermissionList[index]
                assertThat(actualTokenDataPermission["id"]).isEqualTo(permission.id)
                assertThat(actualTokenDataPermission["name"]).isEqualTo(permission.name)
            }
        }

        verify(exactly = 1) { keyManager.getKey() }
    }
}
