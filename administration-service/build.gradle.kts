import by.anatolyloyko.ams.tasks.GenerateDatabaseSchemasTask
import org.springframework.boot.gradle.tasks.bundling.BootJar

group = "by.anatolyloyko.ams"
version = "unspecified"

plugins {
    kotlin("jvm")
    kotlin("plugin.spring")

    id("org.springframework.boot")
    id("io.spring.dependency-management")
}

val jdkVersion: Int = (project.findProperty("jdkVersion") as String).toInt()

kotlin {
    jvmToolchain(jdkVersion)
}

dependencies {
    implementation(project(":common")) {
        exclude(group = "org.jetbrains.exposed")
    }

    testImplementation(testFixtures(project(":common")))
}

sourceSets {
    main {
        resources {
            srcDir("../graphql/common")
            srcDir("../graphql/administration")
        }
        resources {
        }
    }
    test {
        resources {
            srcDir("../graphql/common")
            srcDir("../graphql/administration")
        }
    }
}

tasks.withType<ProcessResources> {
    from("../graphql/brand/graphql") {
        include("**/brands.*").into("graphql/brand")
    }
    from("../graphql/studio/graphql") {
        include("**/studios.*").into("graphql/brand/studio")
    }
    from("../graphql/user/graphql") {
        include("**/users.*").into("graphql/user")
    }
}

tasks.register("generateDatabaseSchema") {
    group = "generation"
    description = "Generate Kotlin classes representing database tables based on a database schema."

    doLast {
        GenerateDatabaseSchemasTask(project).execute("administration")
    }
}

tasks.withType<BootJar> {
    mainClass.set("${project.group}.administration.AdministrationServiceApplication")
}

tasks.test {
    useJUnitPlatform()
}
