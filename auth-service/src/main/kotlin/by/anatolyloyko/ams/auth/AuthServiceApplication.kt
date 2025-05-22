package by.anatolyloyko.ams.auth

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.boot.runApplication
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@ComponentScan(basePackages = ["by.anatolyloyko.ams"])
class AuthServiceApplication : SpringBootServletInitializer() {
    override fun configure(application: SpringApplicationBuilder): SpringApplicationBuilder =
        application.sources(javaClass)
}

fun main(args: Array<String>) {
    runApplication<AuthServiceApplication>(*args)
}
