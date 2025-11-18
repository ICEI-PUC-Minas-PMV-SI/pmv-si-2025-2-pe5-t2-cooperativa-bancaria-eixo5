package com.br.pucBank.data.database

import com.br.pucBank.utils.Logger
import org.jetbrains.exposed.sql.Database
import io.ktor.server.application.*
import java.sql.DriverManager

object DatabaseFactory {
    fun configure(environment: ApplicationEnvironment) {
        val url = System.getenv("DB_URL") ?: environment.config.property("db.url").getString()
        val user = System.getenv("DB_USER") ?: environment.config.property("db.user").getString()
        val password = System.getenv("DB_PASSWORD") ?: environment.config.property("db.password").getString()

        val flywayLocations =
            System.getenv("FLYWAY_LOCATIONS") ?: environment.config.property("flyway.locations").getString()

        Logger.i { "=== DIAGNÓSTICO COMPLETO FLYWAY ===" }
        Logger.i { "📍 URL: $url" }
        Logger.i { "📍 Locations: $flywayLocations" }

        try {
            Class.forName("com.mysql.cj.jdbc.Driver")
            Logger.i { "✅ Driver MySQL carregado" }

            DriverManager.getConnection(url, user, password).use {
                Logger.i { "✅ Conexão MySQL OK" }
            }

            Database.connect(
                url = url,
                driver = "com.mysql.cj.jdbc.Driver",
                user = user,
                password = password
            )

            Logger.i { "🎉 DatabaseFactory configurado com sucesso!" }

        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }
}