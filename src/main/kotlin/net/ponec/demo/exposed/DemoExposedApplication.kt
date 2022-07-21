package net.ponec.demo.exposed

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.spring.SpringTransactionManager
import org.jetbrains.exposed.sql.Database
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.annotation.EnableTransactionManagement


@SpringBootApplication
@EnableTransactionManagement
class DemoExposedApplication

fun main(args: Array<String>) {
    runApplication<DemoExposedApplication>(*args)
}

@Configuration
public class Configuration() {
    @Bean
    fun transactionManager(dataSource: HikariDataSource): SpringTransactionManager {
        return SpringTransactionManager(dataSource)
    }


//    /** H2 database connection */
//    @Bean
//    fun hikariDataSource(): HikariDataSource {
//        val config = HikariConfig()
//        config.jdbcUrl = "jdbc:h2:mem:test"
//        config.username = "sa"
//        config.password = ""
//        config.driverClassName = "org.h2.Driver"
//
//        val hikariDataSource = HikariDataSource(config)
//        Database.connect(hikariDataSource)
//
//        return hikariDataSource;
//    }

//    /** Postgre database connection */
//	@Bean
//	fun  pgsqlHitachiConnect() : HikariDataSource {
//		val config = HikariConfig()
//		config.jdbcUrl = "jdbc:postgresql://127.0.0.1:5432/exposed"
//		config.username = "exposed"
//		config.password = "exposed"
//		config.driverClassName = "org.postgresql.Driver"
//
//		val hikariDataSource = HikariDataSource(config)
//		return hikariDataSource;
//	}


}
