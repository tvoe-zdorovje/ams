// The settings file is the entry point of every Gradle build.
// Its primary purpose is to define the subprojects.
// It is also used for some aspects of project-wide configuration, like managing plugins, dependencies, etc.
// https://docs.gradle.org/current/userguide/settings_file_basics.html

rootProject.name = "ams"

include(
    "common",
    "graphql",
    "user-service",
    "brand-service",
    "studio-service",
)

dependencyResolutionManagement {
    // Use Maven Central as the default repository (where Gradle will download dependencies) in all subprojects.
    @Suppress("UnstableApiUsage")
    repositories {
        mavenCentral()
    }
}

pluginManagement {
    plugins {
        kotlin("jvm") version "2.1.10"
        kotlin("plugin.spring") version "2.1.0"

        id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"

        id("org.springframework.boot") version "3.4.2"
        id("io.spring.dependency-management") version "1.1.7"
    }
}
