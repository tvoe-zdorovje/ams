import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.extensions.DetektExtension.Companion.DEFAULT_SRC_DIR_JAVA
import io.gitlab.arturbosch.detekt.extensions.DetektExtension.Companion.DEFAULT_SRC_DIR_KOTLIN
import io.gitlab.arturbosch.detekt.extensions.DetektExtension.Companion.DEFAULT_TEST_SRC_DIR_JAVA
import io.gitlab.arturbosch.detekt.extensions.DetektExtension.Companion.DEFAULT_TEST_SRC_DIR_KOTLIN
import java.util.Properties

plugins {
    kotlin("jvm") version "2.1.10"

    id("io.gitlab.arturbosch.detekt") version "1.23.8"
}

repositories {
    mavenCentral()
}

val properties = file("../gradle.properties")
    .inputStream()
    .use { stream -> Properties().also { it.load(stream) } }

val databaseDriver: String by properties

val jooqVersion: String by properties

val mockkVersion: String by properties
val kotlinpoetVersion: String by properties
val exposedSpringBootStarterVersion: String by properties
val snakeyamlVersion: String = "2.0"
val detektVersion: String by properties

val junitVersion: String = "5.7.2"
val assertJVersion: String = "3.27.3"

dependencies {
    implementation(
        group = "org.jetbrains.exposed",
        name = "exposed-spring-boot-starter",
        version = exposedSpringBootStarterVersion
    )
    implementation(group = "org.jooq", name = "jooq-meta", version = jooqVersion)
    implementation(group = "org.jooq", name = "jooq-codegen", version = jooqVersion)
    implementation(group = "com.squareup", name = "kotlinpoet", version = kotlinpoetVersion)
    implementation(databaseDriver)

    runtimeOnly(group = "org.yaml", name = "snakeyaml", version = snakeyamlVersion)

    detektPlugins(group = "io.gitlab.arturbosch.detekt", name = "detekt-formatting", version = detektVersion)

    testImplementation(group = "io.mockk", name = "mockk", version = mockkVersion)
    testImplementation(group = "org.junit.jupiter", name = "junit-jupiter-api", version = junitVersion)
    testImplementation(group = "org.junit.jupiter", name = "junit-jupiter-engine", version = junitVersion)
    testImplementation(group = "org.assertj", name = "assertj-core", version = assertJVersion)

}

detekt {
    basePath = rootDir.toString()
    parallel = true
    buildUponDefaultConfig = false
    config.setFrom(files("$rootDir/detekt.yml"))
    ignoreFailures = false

    val modules = listOf(
        "common",
        "buildSrc",
        "user-service",
        "brand-service",
        "studio-service",
        "appointment-service",
        "administration-service"
    )
    source.setFrom(
        modules
            .flatMap {
                listOf(
                    "../$it/$DEFAULT_SRC_DIR_KOTLIN",
                    "../$it/$DEFAULT_TEST_SRC_DIR_KOTLIN",
                    "../$it/$DEFAULT_SRC_DIR_JAVA",
                    "../$it/$DEFAULT_TEST_SRC_DIR_JAVA",
                )
            }
    )
}

tasks.withType<Detekt> {
    exclude("**/orm/jooq/schemas/**")
    exclude("**/orm/exposed/schemas/**")
}

tasks.test {
    useJUnitPlatform()
}
