package com.br.pucBank.routes

import com.br.com.br.pucBank.utils.Logger
import com.br.pucBank.data.repository.clients.ClientRepository
import com.br.pucBank.domain.dto.ClientDTO
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement
import org.koin.ktor.ext.inject

fun Route.clientRoutes() {
    val repository: ClientRepository by inject()

    get("/clients") {
        Logger.i { "GET - /clients" }

        val clients = repository.getAll()

        call.respond(clients)

        Logger.i {
            "GET - /clients responding with ${Json.encodeToJsonElement(clients)}"
        }
    }

    get("/clients/{id}") {
        val id = call.parameters["id"]?.toInt() ?: return@get
        val client = repository.getById(id)

        call.respond(client ?: "Cliente não encontrado")
    }

    post("/clients") {
        val clientDTO = call.receive<ClientDTO>()

        val result = repository.create(clientDTO)

        call.respond(result ?: "Cliente não foi criado")
    }

    post("/clients{id}") {
        val clientDTO = call.receive<ClientDTO>()

        clientDTO.id?.let { id ->
            val result = repository.update(
                id = id,
                clientDto = clientDTO)

            call.respond(result)
        }
    }

    delete("/clients{id}") {
        val id = call.parameters["id"]?.toInt() ?: return@delete

        val deleted = repository.delete(id)

        if (deleted) {
            call.respond("Cliente removido")
        } else {
            call.respond("Cliente Não foi removido")
        }
    }
}