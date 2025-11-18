package com.br.pucBank.data.database

import org.flywaydb.core.Flyway
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

        println("=== DIAGNÓSTICO FLYWAY ===")
        println("URL: $url")
        println("Flyway Locations: $flywayLocations")

        try {
            Class.forName("com.mysql.cj.jdbc.Driver")
            println("✅ Driver MySQL carregado")

            DriverManager.getConnection(url, user, password).use {
                println("✅ Conexão básica com MySQL bem-sucedida!")
            }

            println("⏳ Flyway version: ${Flyway::class.java.`package`.implementationVersion}")

            println("⏳ Configurando Flyway...")
            val flyway = Flyway.configure()
                .dataSource(url, user, password)
                .locations(flywayLocations)
                .baselineOnMigrate(true)
                .baselineVersion("0")
                .validateOnMigrate(true)
                .load()

            println("✅ Flyway configurado, executando migrações...")
            val migrationsApplied = flyway.migrate()
            println("✅ Migrações aplicadas: $migrationsApplied")

            Database.connect(
                url = url,
                driver = "com.mysql.cj.jdbc.Driver",
                user = user,
                password = password
            )
            println("✅ Exposed conectado com sucesso!")

        } catch (e: Exception) {
            println("❌ ERRO CRÍTICO: ${e.javaClass.name}")
            println("❌ Mensagem: ${e.message}")
            e.printStackTrace()
            throw e
        }
    }
}