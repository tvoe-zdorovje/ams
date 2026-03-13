import by.anatolyloyko.ams.tasks.GenerateDatabaseSchemasTask
import com.github.davidmc24.gradle.plugin.avro.GenerateAvroJavaTask
import org.springframework.boot.gradle.tasks.bundling.BootJar

group = "by.anatolyloyko.ams"
version = "unspecified"

plugins {
    kotlin("jvm")
    kotlin("plugin.spring")

    id("org.springframework.boot")
    id("io.spring.dependency-management")

    id("com.github.davidmc24.gradle.plugin.avro") version "1.9.1"

    war
}

val jdkVersion: Int = (project.findProperty("jdkVersion") as String).toInt()
val confluentVersion: String by project
val apacheAvroVersion: String by project

kotlin {
    jvmToolchain(jdkVersion)
}

dependencies {
    implementation(project(":common")) {
        exclude(group = "org.jetbrains.exposed")
    }

    implementation("org.springframework.kafka:spring-kafka")
    implementation(group = "io.confluent", name = "kafka-avro-serializer", version = confluentVersion)
    implementation(group = "org.apache.avro", name = "avro", version = apacheAvroVersion)

    testAndDevelopmentOnly("org.springframework.boot:spring-boot-starter-jetty")

    testImplementation(testFixtures(project(":common")))
}

sourceSets {
    main {
        resources {
            srcDir("../graphql/common")
            srcDir("../graphql/brand")
        }
    }
    test {
        resources {
            srcDir("../graphql/common")
            srcDir("../graphql/brand")
        }
    }
}

tasks.register("generateDatabaseSchema") {
    group = "source generation"
    description = "Generate Kotlin classes representing database tables based on a database schema."

    doLast {
        GenerateDatabaseSchemasTask(project).execute("brands")
    }
}

tasks.named<GenerateAvroJavaTask>("generateAvroJava") {
    source(
        fileTree(rootProject.layout.projectDirectory.dir("infrastructure/kafka/schema-registry/brand")) {
            include("**/*.avsc")
        }
    )
}

tasks.named("compileKotlin") {
    dependsOn("generateAvroJava")
}

tasks.withType<BootJar> {
    enabled = false
}

tasks.test {
    useJUnitPlatform()
}
