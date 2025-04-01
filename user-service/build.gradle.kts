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
}

val jdkVersion: Int = (project.findProperty("jdkVersion") as String).toInt()

kotlin {
    jvmToolchain(jdkVersion)
}

dependencies {
    implementation(project(":common")) {
        exclude(group = "org.jooq")
    }

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
    mainClass.set("${project.group}.user.UserServiceApplication")
}

tasks.test {
    useJUnitPlatform()
}
