package by.anatolyloyko.ams.auth.config

import com.zaxxer.hikari.HikariDataSource
import org.jooq.ConnectionProvider
import org.jooq.DSLContext
import org.jooq.impl.DSL
import org.jooq.impl.DataSourceConnectionProvider
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.jooq.JooqAutoConfiguration
import org.springframework.boot.autoconfigure.jooq.JooqProperties
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.jdbc.datasource.DataSourceTransactionManager
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy
import javax.sql.DataSource

@Configuration
@EnableAutoConfiguration(exclude = [JooqAutoConfiguration::class])
class DatabaseConfiguration {
    @Bean
    @Primary
    @ConfigurationProperties("spring.datasource.administration-db")
    fun administrationDataSource(): DataSource = DataSourceBuilder
        .create()
        .type(HikariDataSource::class.java)
        .build()

    @Bean
    @Primary
    fun administrationTransactionManager(administrationDataSource: DataSource): DataSourceTransactionManager =
        DataSourceTransactionManager(administrationDataSource)

    @Bean
    @Primary
    fun administrationConnectionProvider(administrationDataSource: DataSource): ConnectionProvider =
        DataSourceConnectionProvider(TransactionAwareDataSourceProxy(administrationDataSource))

    @Bean
    @Primary
    fun administrationDslContext(administrationDataSource: DataSource): DSLContext = DSL.using(
        administrationDataSource,
        JooqProperties().determineSqlDialect(administrationDataSource)
    )

    @Bean
    @ConfigurationProperties("spring.datasource.user-db")
    fun userDataSource(): DataSource = DataSourceBuilder
        .create()
        .type(HikariDataSource::class.java)
        .build()

    @Bean
    fun userTransactionManager(@Qualifier("userDataSource") userDataSource: DataSource): DataSourceTransactionManager =
        DataSourceTransactionManager(userDataSource)

    @Bean
    @Primary
    fun userConnectionProvider(@Qualifier("userDataSource") userDataSource: DataSource): ConnectionProvider =
        DataSourceConnectionProvider(TransactionAwareDataSourceProxy(userDataSource))

    @Bean
    fun userDslContext(@Qualifier("userDataSource") userDataSource: DataSource): DSLContext = DSL.using(
        userDataSource,
        JooqProperties().determineSqlDialect(userDataSource)
    )
}
