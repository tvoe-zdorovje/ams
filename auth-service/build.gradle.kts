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

val databaseDriver: String by project

dependencies {
    implementation("org.springframework.boot:spring-boot-starter") // TODO do not use spring
    implementation("org.springframework.boot:spring-boot-starter-logging")
    implementation("org.springframework.boot:spring-boot-starter-graphql")
    implementation("org.springframework.boot:spring-boot-starter-web") {
        exclude(group = "org.springframework.boot", module = "spring-boot-starter-tomcat")
    }
    testAndDevelopmentOnly("org.springframework.boot:spring-boot-starter-jetty")
    implementation("org.springframework.boot:spring-boot-starter-jooq")

    implementation(databaseDriver)
}

sourceSets {
    main {
        resources {
            srcDir("../graphql/common")
            srcDir("../graphql/auth")
        }
    }
    test {
        resources {
            srcDir("../graphql/common")
            srcDir("../graphql/auth")
        }
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
    enabled = false
}

tasks.test {
    useJUnitPlatform()
}
