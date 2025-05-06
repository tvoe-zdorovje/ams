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

val jdkVersion: Int = (project.findProperty("jdkVersion") as String).toInt()

kotlin {
    jvmToolchain(jdkVersion)
}

val databaseDriver: String by project

val exposedSpringBootStarterVersion: String by project
val logbookSpringBootStarterVersion: String by project

val mockkVersion: String by project
val springMockkVersion: String by project
val h2Version: String by project

dependencies {
    api(kotlin("stdlib"))

    api("org.springframework.boot:spring-boot-starter")
    api("org.springframework.boot:spring-boot-starter-logging")
    api("org.springframework.boot:spring-boot-starter-graphql")
    api("org.springframework.boot:spring-boot-starter-web")
    api("org.springframework.boot:spring-boot-starter-jooq")

    api(
        group = "org.jetbrains.exposed",
        name = "exposed-spring-boot-starter",
        version = exposedSpringBootStarterVersion
    )
    api(group = "org.zalando", name = "logbook-spring-boot-starter", version = logbookSpringBootStarterVersion)

    compileOnly("org.springframework.graphql:spring-graphql-test")

    api(databaseDriver)


    testFixturesApi("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.mockito")
    }
    testFixturesApi("org.springframework.graphql:spring-graphql-test")
    testFixturesApi("org.springframework.boot:spring-boot-starter-webflux")

    testFixturesApi(group = "io.mockk", name = "mockk", version = mockkVersion)
    testFixturesApi(group = "com.ninja-squad", name = "springmockk", version = springMockkVersion)

    testFixturesApi("com.h2database", "h2", h2Version)
}

tasks.withType<JacocoCoverageVerification> {
    afterEvaluate {
        classDirectories.setFrom(
            files(
                classDirectories.files.map {
                    fileTree(it) {
                        exclude(
                            "**/FunctionExtensionsKt*",
                            "**/ExposedFinder*",
                            "**/ConditionsKt*",
                            "**/ExtensionsKt*",
                            "**/GraphQlTesterExtensionsKt*"
                        )
                    }
                }
            )
        )
    }
}

tasks.withType<BootJar> {
    enabled = false
}

tasks.test {
    useJUnitPlatform()
}
