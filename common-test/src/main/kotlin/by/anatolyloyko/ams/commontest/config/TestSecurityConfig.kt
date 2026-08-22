package by.anatolyloyko.ams.commontest.config

import by.anatolyloyko.ams.commontest.graphql.RsaJwtTestUtils
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder

@TestConfiguration
class TestSecurityConfig {
    @Bean
    @Primary
    fun jwtDecoder(): JwtDecoder = NimbusJwtDecoder.withPublicKey(RsaJwtTestUtils.PUBLIC_KEY).build()
}
