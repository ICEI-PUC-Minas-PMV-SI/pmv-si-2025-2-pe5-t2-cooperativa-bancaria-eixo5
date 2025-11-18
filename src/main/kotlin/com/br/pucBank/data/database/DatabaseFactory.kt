package com.br.pucBank.data.database

import com.br.com.br.pucBank.utils.Logger
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

        Logger.i { "=== DIAGNÓSTICO COMPLETO FLYWAY ===" }
        Logger.i { "📍 URL: $url" }
        Logger.i { "📍 Locations: $flywayLocations" }

        try {
            Class.forName("com.mysql.cj.jdbc.Driver")
            Logger.i { "✅ Driver MySQL carregado" }

            DriverManager.getConnection(url, user, password).use {
                Logger.i { "✅ Conexão MySQL OK" }
            }

            Logger.i { "📋 Listando todas as migrações disponíveis..." }
            try {
                val resources = javaClass.classLoader.getResources("db/migration")
                var foundAny = false

                while (resources.hasMoreElements()) {
                    val resource = resources.nextElement()
                    Logger.i { resource.toString() }
                    foundAny = true
                }

                if (!foundAny) {
                    Logger.e("❌ NENHUM arquivo de migração encontrado!")
                }

            } catch (e: Exception) {
                Logger.e("❌ Erro ao listar migrações: ${e.message}")
            }

            println("⏳ Configurando Flyway...")
            val flyway = Flyway.configure()
                .dataSource(url, user, password)
                .locations(flywayLocations)
                .validateMigrationNaming(true)
                .validateOnMigrate(true)
                .baselineOnMigrate(true)
                .baselineVersion("0")
                .load()

            // ✅ INFORMAÇÕES DETALHADAS
            val info = flyway.info()
            Logger.i { "📊 Status do Flyway:" }
            Logger.i { "- Migrações aplicadas: ${info.applied().size}" }

            if (info.pending().isEmpty()) {
                Logger.w { "🚨 ALERTA: Nenhuma migração pendente!" }
                Logger.w { "🚨 O Flyway não está encontrando seu arquivo SQL!" }
            }

            val result = flyway.migrate()
            Logger.i { "✅ Resultado: ${result.migrationsExecuted} migrações executadas" }

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