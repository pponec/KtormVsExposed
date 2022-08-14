package net.ponec.demo

import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.spring.SpringTransactionManager
import org.jetbrains.exposed.sql.Database
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.annotation.EnableTransactionManagement
import javax.sql.DataSource


@SpringBootApplication
@EnableTransactionManagement
class DemoApplication

fun main(args: Array<String>) {
    runApplication<DemoApplication>(*args)
}

@Configuration
class Configuration {
    @Bean
    fun transactionManager(dataSource: HikariDataSource): SpringTransactionManager {
        return SpringTransactionManager(dataSource)
    }

    // ---- Ktorm configuration ---

    /**
     * Register the [Database] instance as a Spring bean.
     */
    @Bean
    fun database(dataSource: DataSource): org.ktorm.database.Database {
        return org.ktorm.database.Database.connectWithSpringSupport(dataSource)
    }

//    /**
//     * Register Ktorm's Jackson extension to the Spring's container
//     * so that we can serialize/deserialize Ktorm entities.
//     */
//    @Bean
//    fun ktormModule(): Module {
//        return KtormModule()
//    }


}
