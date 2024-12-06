import org.springframework.boot.gradle.tasks.bundling.BootJar

group = "by.anatolyloyko.ams"
version = "unspecified"

plugins {
    `java-library`
    `java-test-fixtures`
    kotlin("jvm")
    kotlin("plugin.spring")

    id("org.springframework.boot")
    id("io.spring.dependency-management")
}

kotlin {
    jvmToolchain(17)
}

dependencies {
    api(kotlin("stdlib"))

    api("org.springframework.boot:spring-boot-starter")
    api("org.springframework.boot:spring-boot-starter-logging")
    api("org.springframework.boot:spring-boot-starter-graphql")
    api("org.springframework.boot:spring-boot-starter-web")
    compileOnly("org.springframework.graphql:spring-graphql-test")

    api(group = "org.zalando", name = "logbook-spring-boot-starter", version = "3.10.0")


    testFixturesApi("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.mockito")
    }
    testFixturesApi("org.springframework.graphql:spring-graphql-test")
    testFixturesApi("org.springframework.boot:spring-boot-starter-webflux")

    testFixturesApi(group = "io.mockk", name = "mockk", version = "1.13.16")
    testFixturesApi(group = "com.ninja-squad", name = "springmockk", version = "4.0.2")
}

tasks.withType<BootJar> {
    enabled = false
}

tasks.test {
    useJUnitPlatform()
}
