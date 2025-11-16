package com.br.pucBank

import com.br.com.br.pucBank.routes.clientRoutes
import com.br.pucBank.data.database.DatabaseFactory
import com.br.pucBank.data.di.pucBankDataModules
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.routing.routing
import kotlinx.serialization.json.Json
import org.koin.ktor.plugin.Koin

fun main() {
    DatabaseFactory.init()

    embeddedServer(Netty, port = 8080) {
        install(ContentNegotiation) {
            json(Json { prettyPrint = true })
        }

        install(Koin) {
            modules(
                pucBankDataModules
            )
        }

        routing {
            clientRoutes()
        }
    }.start(wait = true)
}