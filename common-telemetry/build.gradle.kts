group = "by.anatolyloyko.ams"
version = "0.0.1-SNAPSHOT"

plugins {
    `java-library`
}

val opentelemetryInstrumentationBomVersion: String by project
val micrometerPrometheusVersion: String by project

dependencies {
    implementation(platform("io.opentelemetry.instrumentation:opentelemetry-instrumentation-bom:$opentelemetryInstrumentationBomVersion"))

    api("org.springframework.boot:spring-boot-starter-actuator")
    api("io.micrometer:micrometer-registry-prometheus:$micrometerPrometheusVersion")
    api("io.opentelemetry.instrumentation:opentelemetry-spring-boot-starter")
    api("io.opentelemetry:opentelemetry-exporter-otlp")
//    api("io.micrometer:context-propagation:1.2.1")


}

