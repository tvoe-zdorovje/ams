package by.anatolyloyko.ams.appointment

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@ComponentScan(basePackages = ["by.anatolyloyko.ams"])
class AppointmentServiceApplication

fun main(args: Array<String>) {
    runApplication<AppointmentServiceApplication>(*args)
}
