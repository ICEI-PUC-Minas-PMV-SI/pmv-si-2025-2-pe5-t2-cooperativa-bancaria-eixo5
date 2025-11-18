package com.br.pucBank.routes

import com.br.pucBank.data.repository.clients.ClientRepository
import com.br.pucBank.domain.clients.models.ClientRequest
import com.br.pucBank.utils.defaultClientNotFoundResponse
import com.br.pucBank.utils.resultFailed
import com.br.pucBank.utils.resultSuccess
import com.br.pucBank.utils.validateAuthentication
import io.ktor.http.*
import io.ktor.server.auth.authenticate
import io.ktor.server.request.receive
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.clientRoutes() {
    val repository: ClientRepository by inject()

    post("/clients") {
        val body = runCatching { call.receive<ClientRequest>() }
            .getOrElse {
                return@post call.resultFailed(
                    message = "Dados inválidos",
                    status = HttpStatusCode.BadRequest
                )
            }

        val created = repository.create(body)

        if (created != null) {
            call.resultSuccess(created)
        } else {
            call.resultFailed(
                message = "Não foi possível criar o cliente",
                status = HttpStatusCode.InternalServerError
            )
        }
    }

    authenticate {
        get("/clients/me") {
            val principal = call.validateAuthentication()

            val clientId = principal.payload.getClaim("id").asString()

            val client = repository.getById(clientId)

            if (client != null) {
                call.resultSuccess(client)
            } else {
                call.defaultClientNotFoundResponse()
            }
        }

        put("/clients/me") {
            val principal = call.validateAuthentication()

            val clientId = principal.payload.getClaim("id").asString()

            val body = call.receive<ClientRequest>()

            val updated = repository.update(clientId, body)

            if (updated) {
                call.resultSuccess(message = "Cliente atualizado com sucesso")
            } else {
                call.defaultClientNotFoundResponse()
            }
        }

        delete("/clients/me") {
            val principal = call.validateAuthentication()

            val clientId = principal.payload.getClaim("id").asString()

            val removed = repository.delete(clientId)

            if (removed) {
                call.resultSuccess(message = "Cliente removido com sucesso")
            } else {
                call.defaultClientNotFoundResponse()
            }
        }
    }
}