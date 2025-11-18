package com.br.pucBank

import com.br.pucBank.security.AuthenticationFactory
import com.br.pucBank.routes.clientRoutes
import com.br.pucBank.data.database.DatabaseFactory
import com.br.pucBank.data.di.pucBankDataModules
import com.br.pucBank.domain.di.pucBankDomainModules
import com.br.pucBank.routes.loginRoutes
import com.br.pucBank.security.pucBankSecurityModules
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import kotlinx.serialization.json.Json
import org.koin.ktor.plugin.Koin

fun main() {
    embeddedServer(Netty, port = 8080) {
        DatabaseFactory.configure(environment)

        install(ContentNegotiation) {
            json(Json { prettyPrint = true })
        }

        install(Koin) {
            modules(
                pucBankSecurityModules,
                pucBankDomainModules,
                pucBankDataModules
            )
        }

        AuthenticationFactory(this).also {
            it.configure(this)
        }

        routing {
            get("/health") {
                call.respond(
                    status = HttpStatusCode.OK,
                    message = "OK"
                )
            }

            loginRoutes()
            clientRoutes()
        }
    }.start(wait = true)
}