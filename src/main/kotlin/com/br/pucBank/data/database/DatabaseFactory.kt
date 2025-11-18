package com.br.pucBank.data.database

import com.br.pucBank.utils.Logger
import org.jetbrains.exposed.sql.Database
import io.ktor.server.application.*
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import javax.sql.DataSource

object DatabaseFactory {

    fun configure(environment: ApplicationEnvironment) {
        val url = System.getenv("DB_URL") ?: environment.config.property("db.url").getString()
        val user = System.getenv("DB_USER") ?: environment.config.property("db.user").getString()
        val password = System.getenv("DB_PASSWORD") ?: environment.config.property("db.password").getString()
        val flywayLocations =
            System.getenv("FLYWAY_LOCATIONS") ?: environment.config.property("flyway.locations").getString()

        Logger.i { "=== CONFIGURAÇÃO DATABASE ===" }
        Logger.i { "📍 URL: $url" }
        Logger.i { "📍 Flyway Locations: $flywayLocations" }

        try {
            val dataSource = createHikariDataSource(
                url = url,
                user = user,
                sqlPassword = password
            )
            Logger.i { "✅ HikariCP DataSource criado" }

            Database.connect(
                datasource = dataSource
            )
            Logger.i { "✅ Exposed conectado ao DataSource" }

            Logger.i { "🎉 DatabaseFactory configurado com sucesso!" }

        } catch (e: Exception) {
            Logger.e("❌ ${e.message}")
            e.printStackTrace()
            throw e
        }
    }

    private fun createHikariDataSource(url: String, user: String, sqlPassword: String): DataSource {
        return HikariDataSource(HikariConfig().apply {
            jdbcUrl = url
            username = user
            password = sqlPassword
            driverClassName = "com.mysql.cj.jdbc.Driver"
            maximumPoolSize = 10
            minimumIdle = 2
            connectionTimeout = 30000

            addDataSourceProperty("cachePrepStmts", "true")
            addDataSourceProperty("prepStmtCacheSize", "250")
            addDataSourceProperty("prepStmtCacheSqlLimit", "2048")
        })
    }
}