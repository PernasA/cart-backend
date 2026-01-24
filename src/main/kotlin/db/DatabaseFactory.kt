package com.db

import com.typesafe.config.ConfigFactory
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.config.HoconApplicationConfig
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.insertIgnore
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.Instant
import java.util.UUID

object DatabaseFactory {

    fun init() {
        val jdbcUrlSystem = System.getenv("DATABASE_URL")
            ?: "jdbc:postgresql://localhost:5432/cart_db"

        val user = System.getenv("DB_USER") ?: error("DB_USER not set")
        val passwordSystem = System.getenv("DB_PASSWORD") ?: error("DB_PASSWORD not set")

        val hikariConfig = HikariConfig().apply {
            jdbcUrl = jdbcUrlSystem
            username = user
            password = passwordSystem
            driverClassName = "org.postgresql.Driver"
            maximumPoolSize = 5
            isAutoCommit = false
            transactionIsolation = "TRANSACTION_REPEATABLE_READ"
        }

        val dataSource = HikariDataSource(hikariConfig)

        Database.connect(dataSource)

        transaction {
            SchemaUtils.createMissingTablesAndColumns(UsersTable)
            SchemaUtils.createMissingTablesAndColumns(CartsTable)
        }
        transaction {
            UsersTable.insertIgnore {
                it[id] = UUID.fromString("00000000-0000-0000-0000-000000000001")
                it[firebaseUid] = "local_dev_uid"
                it[email] = "dev@test.com"
                it[displayName] = "Local Dev User"
                it[createdAt] = Instant.now()
            }
        }
    }
}

object ApplicationConfigProvider {
    fun dbUrl() = HoconApplicationConfig(ConfigFactory.load()).property("db.url").getString()
    fun dbUser() = HoconApplicationConfig(ConfigFactory.load()).property("db.user").getString()
    fun dbPassword() = HoconApplicationConfig(ConfigFactory.load()).property("db.password").getString()
}
