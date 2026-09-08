import org.gradle.kotlin.dsl.withType
import org.springframework.boot.gradle.tasks.bundling.BootJar

group = "by.anatolyloyko.ams"
version = "0.0.1-SNAPSHOT"

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


val mockkVersion: String by project
val springMockkVersion: String by project
val h2Version: String by project

dependencies {
    compileOnly(project(":common"))

    api("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.mockito")
    }
    api("org.springframework.graphql:spring-graphql-test")
    api("org.springframework.boot:spring-boot-starter-webflux")
    api("io.projectreactor:reactor-test")

    api(group = "io.mockk", name = "mockk", version = mockkVersion)
    api(group = "com.ninja-squad", name = "springmockk", version = springMockkVersion)

    api("com.h2database", "h2", h2Version)
}

tasks.register("prepareKotlinBuildScriptModel"){
    description = "a great fix I googled"
}

tasks.withType<BootJar> {
    enabled = false
}

