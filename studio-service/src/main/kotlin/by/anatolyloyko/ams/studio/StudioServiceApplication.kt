package by.anatolyloyko.ams.studio

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@ComponentScan(basePackages = ["by.anatolyloyko.ams"])
class StudioServiceApplication

fun main(args: Array<String>) {
    runApplication<StudioServiceApplication>(*args)
}
