package by.anatolyloyko.ams.auth.token.jwt.key.jwks.http

import by.anatolyloyko.ams.auth.token.jwt.key.KeyManager
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * The REST controller that exposes access to JSON Web Key Set (JWKS).
 */
@RestController
@RequestMapping("/.well-known/jwks")
class JwksRestController(
    private val keyManager: KeyManager
) {
    /**
     * The endpoint that exposes access to JSON Web Key Set (JWKS).
     *
     * @return JWKS in JSON format
     */
    @GetMapping
    fun getJwkSet(): MutableMap<String, Any> = keyManager
        .getJwkSet()
        .toJSONObject(true)
}
