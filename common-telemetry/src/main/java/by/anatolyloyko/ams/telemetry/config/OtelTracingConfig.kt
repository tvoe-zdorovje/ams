package by.anatolyloyko.ams.telemetry.config

import graphql.execution.instrumentation.Instrumentation
import io.opentelemetry.api.OpenTelemetry
import io.opentelemetry.api.trace.SpanKind
import io.opentelemetry.contrib.sampler.RuleBasedRoutingSampler
import io.opentelemetry.instrumentation.graphql.v20_0.GraphQLTelemetry
import io.opentelemetry.sdk.autoconfigure.spi.AutoConfigurationCustomizer
import io.opentelemetry.sdk.autoconfigure.spi.AutoConfigurationCustomizerProvider
import io.opentelemetry.sdk.autoconfigure.spi.ConfigProperties
import io.opentelemetry.sdk.trace.samplers.Sampler
import io.opentelemetry.semconv.UrlAttributes
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.function.BiFunction


@Configuration
class OtelTracingConfig {
    @Bean
    fun otelCustomizer(): AutoConfigurationCustomizerProvider =
        AutoConfigurationCustomizerProvider { provider: AutoConfigurationCustomizer ->
            provider.addSamplerCustomizer(
                BiFunction { fallbackSampler: Sampler, _: ConfigProperties ->
                    RuleBasedRoutingSampler.builder(SpanKind.SERVER, fallbackSampler)
                        .drop(UrlAttributes.URL_PATH, "^/actuator")
                        .build()
                }
            )
        }

    @Bean
    fun openTelemetryGraphQLInstrumentation(@Qualifier("openTelemetry") openTelemetry: OpenTelemetry): Instrumentation =
        GraphQLTelemetry.builder(openTelemetry)
            .setOperationNameInSpanNameEnabled(true)
            .build()
            .createInstrumentation()
}
