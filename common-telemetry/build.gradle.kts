group = "by.anatolyloyko.ams"
version = "0.0.1-SNAPSHOT"

plugins {
    `java-library`
}

val micrometerPrometheusVersion: String by project

dependencies {
    api(group = "io.micrometer", name = "micrometer-registry-prometheus", version = micrometerPrometheusVersion)
}
