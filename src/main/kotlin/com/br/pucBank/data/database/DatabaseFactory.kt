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

        val flywayLocations = System.getenv("FLYWAY_LOCATIONS") ?: environment.config.property("flyway.locations").getString()

        println("=== DIAGNÓSTICO COMPLETO FLYWAY ===")
        println("📍 URL: $url")
        println("📍 Locations: $flywayLocations")

        try {
            Class.forName("com.mysql.cj.jdbc.Driver")
            println("✅ Driver MySQL carregado")

            DriverManager.getConnection(url, user, password).use {
                println("✅ Conexão MySQL OK")
            }

            println("🔍 Verificando migração no classpath...")
            val migrationFile = "db/migration/V1__Create_clients_table.sql"
            val resourceUrl = javaClass.classLoader.getResource(migrationFile)
            if (resourceUrl != null) {
                println("✅ Arquivo encontrado: $migrationFile")
                println("📍 Localização: $resourceUrl")
            } else {
                println("❌ ARQUIVO NÃO ENCONTRADO: $migrationFile")
                println("💡 O arquivo não está no JAR!")
                println("💡 Verifique se está em: src/main/resources/db/migration/")
                println("💡 Execute: jar tf pucBank-all.jar | grep -i migration")
            }

            println("📋 Listando todas as migrações disponíveis...")
            try {
                val resources = javaClass.classLoader.getResources("db/migration")
                var foundAny = false
                while (resources.hasMoreElements()) {
                    val resource = resources.nextElement()
                    println("   - $resource")
                    foundAny = true
                }
                if (!foundAny) {
                    println("   ❌ NENHUM arquivo de migração encontrado!")
                }
            } catch (e: Exception) {
                println("   ❌ Erro ao listar migrações: ${e.message}")
            }

            println("⏳ Configurando Flyway...")
            val flyway = Flyway.configure()
                .dataSource(url, user, password)
                .locations(flywayLocations)
                .baselineOnMigrate(true)
                .baselineVersion("0")
                .validateOnMigrate(true)
                .load()

            // ✅ INFORMAÇÕES DETALHADAS
            val info = flyway.info()
            println("📊 Status do Flyway:")
            println("   - Migrações aplicadas: ${info.applied().size}")
            info.applied().forEach {
                println("     ✅ ${it.version}: ${it.script}")
            }
            println("   - Migrações pendentes: ${info.pending().size}")
            info.pending().forEach {
                println("     ⏳ ${it.version}: ${it.script}")
            }

            if (info.pending().isEmpty()) {
                println("🚨 ALERTA: Nenhuma migração pendente!")
                println("🚨 O Flyway não está encontrando seu arquivo SQL!")
            }

            println("⏳ Executando migrações...")
            val result = flyway.migrate()
            println("✅ Resultado: ${result.migrationsExecuted} migrações executadas")

            Database.connect(
                url = url,
                driver = "com.mysql.cj.jdbc.Driver",
                user = user,
                password = password
            )
            println("🎉 DatabaseFactory configurado com sucesso!")

        } catch (e: Exception) {
            println("❌ Erro crítico: ${e.message}")
            e.printStackTrace()
            throw e
        }
    }
}