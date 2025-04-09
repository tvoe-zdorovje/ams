plugins {
    kotlin("jvm")
}

tasks {
    register<Copy>("installGitHook") {
        from(file("$rootDir/.git-hooks/pre-commit"))
        into(file("$rootDir/.git/hooks/"))
    }

    named("build") {
        dependsOn("installGitHook")
    }
}
