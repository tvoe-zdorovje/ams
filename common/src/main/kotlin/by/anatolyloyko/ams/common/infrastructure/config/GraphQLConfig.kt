package by.anatolyloyko.ams.common.infrastructure.config

import by.anatolyloyko.ams.common.infrastructure.graphql.directive.RequiresPermissionsDirective
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.graphql.execution.RuntimeWiringConfigurer

@Configuration
class GraphQLConfig {
    @Bean
    fun runtimeWiringConfigurer() = RuntimeWiringConfigurer {
        it.directive("requiresPermissions", RequiresPermissionsDirective())
    }
}
