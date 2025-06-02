package by.anatolyloyko.ams.auth.token.jwt.key.jwks.http

import by.anatolyloyko.ams.auth.token.jwt.key.KeyManager
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/.well-known/jwks")
class JwksRestController(
    private val keyManager: KeyManager
) {
    @GetMapping
    fun getJwkSet(): MutableMap<String, Any> = keyManager
        .getJwkSet()
        .toJSONObject(true)
}
