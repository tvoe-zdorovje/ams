import org.springframework.boot.gradle.tasks.bundling.BootJar

group = "by.anatolyloyko.ams"
version = "0.1.2"

plugins {
    `java-library`
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

val jacksonModuleKotlinVersion: String by project

val mockkVersion: String by project
val springMockkVersion: String by project
val h2Version: String by project

dependencies {
    compileOnly("jakarta.servlet:jakarta.servlet-api:6.0.0")

    api(kotlin("stdlib"))

    api("io.projectreactor.kotlin:reactor-kotlin-extensions")

    api("org.springframework.boot:spring-boot-starter")
    api("org.springframework.boot:spring-boot-starter-actuator")
    api("org.springframework.boot:spring-boot-starter-logging")
    api("org.springframework.boot:spring-boot-starter-graphql")
    api("org.springframework.boot:spring-boot-starter-aop")
    api("org.springframework.cloud:spring-cloud-starter-config")
    api("org.springframework.retry:spring-retry")
    api("org.springframework.boot:spring-boot-starter-web") {
        exclude(group = "org.springframework.boot", module = "spring-boot-starter-tomcat")
    }
    api("org.springframework.boot:spring-boot-starter-jooq")
    api("org.springframework.boot:spring-boot-starter-oauth2-resource-server")

    api(
        group = "org.jetbrains.exposed",
        name = "exposed-spring-boot-starter",
        version = exposedSpringBootStarterVersion
    )
    api(group = "org.zalando", name = "logbook-spring-boot-starter", version = logbookSpringBootStarterVersion)

    api(databaseDriver)

    api(group = "com.fasterxml.jackson.module", name = "jackson-module-kotlin", version = jacksonModuleKotlinVersion)


    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.mockito")
    }
    testImplementation(group = "io.mockk", name = "mockk", version = mockkVersion)
    testImplementation("io.projectreactor:reactor-test")
}

dependencyManagement { // doesn't affect child services, see root build.gradle.kts
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
    }
}

tasks.withType<JacocoCoverageVerification> {
    afterEvaluate {
        classDirectories.setFrom(
            files(
                classDirectories.files.map {
                    fileTree(it) {
                        exclude(
                            "**/*ServiceApplication*",
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
