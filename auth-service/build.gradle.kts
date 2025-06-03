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

val databaseDriver: String by project
val argon2JvmVersion: String by project
val nimbusJoseJwtVersion: String by project
val mockkVersion: String by project
val springMockkVersion: String by project

dependencies {
    implementation("org.springframework.boot:spring-boot-starter") // TODO do not use spring
    implementation("org.springframework.boot:spring-boot-starter-logging")
    implementation("org.springframework.boot:spring-boot-starter-graphql")
    implementation("org.springframework.boot:spring-boot-starter-jooq")
    implementation("org.springframework.boot:spring-boot-starter-web") {
        exclude(group = "org.springframework.boot", module = "spring-boot-starter-tomcat")
    }
    testAndDevelopmentOnly("org.springframework.boot:spring-boot-starter-jetty")

    implementation(databaseDriver)
    implementation(group = "de.mkammerer", name = "argon2-jvm-nolibs", version = argon2JvmVersion)
    implementation(group = "com.nimbusds", name = "nimbus-jose-jwt", version = nimbusJoseJwtVersion)

    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.mockito")
    }
    testImplementation("org.springframework.graphql:spring-graphql-test")
    testImplementation("org.springframework.boot:spring-boot-starter-webflux")
    testImplementation(group = "io.mockk", name = "mockk", version = mockkVersion)
    testImplementation(group = "com.ninja-squad", name = "springmockk", version = springMockkVersion)
    testImplementation(kotlin("test"))
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
