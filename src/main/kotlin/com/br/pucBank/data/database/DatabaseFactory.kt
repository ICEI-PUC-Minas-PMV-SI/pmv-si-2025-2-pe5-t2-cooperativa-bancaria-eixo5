package com.br.pucBank.data.database

import org.flywaydb.core.Flyway
import org.jetbrains.exposed.sql.Database
import io.ktor.server.application.*

object DatabaseFactory {
    fun configure(environment: ApplicationEnvironment) {
        val config = environment.config

        val url = config.property("db.url").getString()
        val user = config.property("db.user").getString()
        val password = config.property("db.password").getString()

        val flyway = Flyway.configure()
            .dataSource(url, user, password)
            .locations(config.property("flyway.locations").getString())
            .baselineOnMigrate(true)
            .baselineVersion("0")
            .validateOnMigrate(true)
            .load()

        flyway.migrate()

        Database.connect(
            url = url,
            driver = "com.mysql.cj.jdbc.Driver",
            user = user,
            password = password
        )
    }
}