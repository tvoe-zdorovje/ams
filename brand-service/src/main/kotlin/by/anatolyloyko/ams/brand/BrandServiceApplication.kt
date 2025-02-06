package by.anatolyloyko.ams.brand

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@ComponentScan(basePackages = ["by.anatolyloyko.ams"])
class BrandServiceApplication

fun main(args: Array<String>) {
    runApplication<BrandServiceApplication>(*args)
}
