package by.anatolyloyko.ams.commontest.graphql

import by.anatolyloyko.ams.commontest.config.TestSecurityConfig
import org.springframework.boot.test.autoconfigure.graphql.tester.AutoConfigureHttpGraphQlTester
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import java.lang.annotation.Inherited

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Inherited
@SpringBootTest
@AutoConfigureHttpGraphQlTester
@Import(TestSecurityConfig::class)
annotation class GraphQLTest
