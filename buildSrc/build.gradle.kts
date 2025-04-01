import java.util.Properties

plugins {
    kotlin("jvm") version "2.1.10"
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

    testImplementation(group = "io.mockk", name = "mockk", version = mockkVersion)
    testImplementation(group = "org.junit.jupiter", name = "junit-jupiter-api", version = junitVersion)
    testImplementation(group = "org.junit.jupiter", name = "junit-jupiter-engine", version = junitVersion)
    testImplementation(group = "org.assertj", name = "assertj-core", version = assertJVersion)

}

tasks.test {
    useJUnitPlatform()
}
