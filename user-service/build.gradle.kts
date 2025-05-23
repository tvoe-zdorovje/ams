import by.anatolyloyko.ams.orm.util.generator.SchemaGenerator
import by.anatolyloyko.ams.tasks.GenerateDatabaseSchemasTask
import org.springframework.boot.gradle.tasks.bundling.BootJar

group = "by.anatolyloyko.ams"
version = "unspecified"

plugins {
    kotlin("jvm")
    kotlin("plugin.spring")

    id("org.springframework.boot")
    id("io.spring.dependency-management")

    war
}

val jdkVersion: Int = (project.findProperty("jdkVersion") as String).toInt()

kotlin {
    jvmToolchain(jdkVersion)
}

val argon2JvmVersion: String by project

dependencies {
    implementation(project(":common")) {
        exclude(module = "spring-boot-starter-jooq")
    }

    implementation(group = "de.mkammerer", name = "argon2-jvm", version = argon2JvmVersion)

    testAndDevelopmentOnly("org.springframework.boot:spring-boot-starter-jetty")

    testImplementation(testFixtures(project(":common")))
}

sourceSets {
    main {
        resources {
            srcDir("../graphql/common")
            srcDir("../graphql/user")
        }
    }
    test {
        resources {
            srcDir("../graphql/common")
            srcDir("../graphql/user")
        }
    }
}

tasks.register("generateDatabaseSchema") {
    group = "generation"
    description = "Generate Kotlin classes representing database tables based on a database schema."

    doLast {
        GenerateDatabaseSchemasTask(project).execute(
            SchemaGenerator.Generators.EXPOSED,
            "users"
        )
    }
}

tasks.withType<BootJar> {
    enabled = false
}

tasks.test {
    useJUnitPlatform()
}
