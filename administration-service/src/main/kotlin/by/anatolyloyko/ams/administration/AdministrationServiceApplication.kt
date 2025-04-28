package by.anatolyloyko.ams.administration

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@ComponentScan(basePackages = ["by.anatolyloyko.ams"])
class AdministrationServiceApplication

fun main(args: Array<String>) {
    runApplication<AdministrationServiceApplication>(*args)
}
