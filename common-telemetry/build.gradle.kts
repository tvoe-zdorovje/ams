import org.gradle.kotlin.dsl.withType
import org.springframework.boot.gradle.tasks.bundling.BootJar

group = "by.anatolyloyko.ams"
version = "0.0.1-SNAPSHOT"

plugins {
    `java-library`
    kotlin("jvm")
    kotlin("plugin.spring")
    id("org.springframework.boot")
}

val jdkVersion: Int = (project.findProperty("jdkVersion") as String).toInt()

kotlin {
    jvmToolchain(jdkVersion)
}

tasks.withType<BootJar> {
    enabled = false
}


val opentelemetryInstrumentationBomVersion: String by project
val micrometerPrometheusVersion: String by project

dependencies {
    api(platform("io.opentelemetry.instrumentation:opentelemetry-instrumentation-bom:$opentelemetryInstrumentationBomVersion"))
    api(platform("io.opentelemetry.instrumentation:opentelemetry-instrumentation-bom-alpha:$opentelemetryInstrumentationBomVersion-alpha"))

    api("org.springframework.boot:spring-boot-starter-actuator")
    api("io.micrometer:micrometer-registry-prometheus:$micrometerPrometheusVersion")
    api("io.opentelemetry.instrumentation:opentelemetry-spring-boot-starter")
    api("io.opentelemetry:opentelemetry-exporter-otlp")
    api("io.opentelemetry.instrumentation:opentelemetry-graphql-java-20.0")
    api("io.opentelemetry.contrib:opentelemetry-samplers:1.62.1-alpha")

    compileOnly("org.springframework.boot:spring-boot-starter")
    compileOnly("org.springframework.boot:spring-boot-starter-graphql")
}
