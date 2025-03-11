package by.anatolyloyko.ams

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class CommonApplication

fun main(args: Array<String>) {
    runApplication<CommonApplication>(*args)
}
