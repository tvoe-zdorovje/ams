import org.springframework.boot.gradle.tasks.bundling.BootJar

group = "by.anatolyloyko.ams"
version = "unspecified"

plugins {
    kotlin("jvm")
    kotlin("plugin.spring")

    id("org.springframework.boot")
    id("io.spring.dependency-management")
}

kotlin {
    jvmToolchain(17)
}

dependencies {
    implementation(project(":common"))

    testImplementation(testFixtures(project(":common")))
}

sourceSets {
    main {
        resources {
            srcDir("../graphql/common")
            srcDir("../graphql/appointment")
        }
    }
    test {
        resources {
            srcDir("../graphql/common")
            srcDir("../graphql/appointment")
        }
    }
}

tasks.withType<BootJar> {
    mainClass.set("by.anatolyloyko.ams.appointment.AppointmentServiceApplication")
}

tasks.test {
    useJUnitPlatform()
}
