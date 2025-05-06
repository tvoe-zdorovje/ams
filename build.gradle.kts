plugins {
    kotlin("jvm")
    jacoco
}

subprojects {
    apply(plugin = "jacoco")
    apply(plugin = "java")

    tasks {
        // === GIT hooks === //

        register<Copy>("installGitHook") {
            from(file("$rootDir/.git-hooks/pre-commit"))
            into(file("$rootDir/.git/hooks/"))
        }
        named("build") {
            dependsOn("installGitHook")
        }

        // === JaCoCo === //

        withType<Test> {
            finalizedBy(jacocoTestReport)
        }

        fun JacocoReportBase.afterEvaluateExcludes() = afterEvaluate {
            classDirectories.setFrom(
                files(
                    classDirectories.files.map {
                        fileTree(it) {
                            exclude(
                                "**/orm/*/schemas/**",
                                "**/*ApplicationKt*"
                            )
                        }
                    }
                )
            )
        }

        withType<JacocoReport> {
            dependsOn(test)

            afterEvaluateExcludes()

            doLast {
                println("View code coverage at:")
                println("file://${layout.buildDirectory.get()}/reports/jacoco/test/html/index.html")
            }
        }

        withType<JacocoCoverageVerification> {
            violationRules {
                rule {
                    limit {
                        minimum = BigDecimal.valueOf(0.9)
                    }
                }
            }

            afterEvaluateExcludes()
        }
    }
}
