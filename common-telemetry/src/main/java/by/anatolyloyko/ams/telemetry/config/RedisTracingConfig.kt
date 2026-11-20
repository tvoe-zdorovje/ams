package by.anatolyloyko.ams.telemetry.config

import io.lettuce.core.resource.ClientResources
import io.opentelemetry.api.OpenTelemetry
import io.opentelemetry.instrumentation.lettuce.v5_1.LettuceTelemetry
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.data.redis.ClientResourcesBuilderCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@ConditionalOnClass(ClientResources::class)
class RedisTracingConfig {
    @Bean
    fun lettuceTracingCustomizer(
        @Qualifier("openTelemetry") openTelemetry: OpenTelemetry
    ): ClientResourcesBuilderCustomizer {
        return ClientResourcesBuilderCustomizer {
            it.tracing( // the only way I found to write spans into parent trace for MVC. Doesn't work for webflux
                LettuceTelemetry
                    .create(openTelemetry)
                    .createTracing()
            )
        }
    }
}
