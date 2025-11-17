package com.br.pucBank.routes

import com.br.pucBank.data.repository.clients.ClientRepository
import com.br.pucBank.domain.dto.ClientDTO
import com.br.pucBank.utils.resultSuccess
import com.br.pucBank.utils.resultFailed
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import org.koin.ktor.ext.inject

fun Route.clientRoutes() {
    val repository: ClientRepository by inject()

    get("/clients") {
        runCatching {
            repository.getAll()
        }.onSuccess { clients ->
            call.resultSuccess(clients)
        }.onFailure { e ->
            call.resultFailed(e.message)
        }
    }


    get("/clients/{id}") {
        val id = call.parameters["id"]?.toIntOrNull() ?: return@get call.resultFailed(
            "ID inválido"
        )

        runCatching {
            repository.getById(id)
        }.onSuccess { client ->
            if (client != null) {
                call.resultSuccess(client)
            } else {
                call.resultFailed("Cliente não encontrado", status = HttpStatusCode.NotFound)
            }
        }.onFailure { e ->
            call.resultFailed(e.message, status = HttpStatusCode.InternalServerError)
        }
    }


    post("/clients") {
        runCatching {
            val clientDTO = call.receive<ClientDTO>()
            repository.create(clientDTO)
        }.onSuccess { result ->
            if (result != null) {
                call.resultSuccess(result, status = HttpStatusCode.Created)
            } else {
                call.resultFailed("Cliente não foi criado")
            }
        }.onFailure { e ->
            call.resultFailed(e.message, status = HttpStatusCode.InternalServerError)
        }
    }


    put("/clients/{id}") {
        val id = call.parameters["id"]?.toIntOrNull() ?: return@put call.resultFailed(
            "ID inválido"
        )

        runCatching {
            val clientDTO = call.receive<ClientDTO>()
            repository.update(id, clientDTO)
        }.onSuccess { result ->
            call.resultSuccess(result)
        }.onFailure { e ->
            call.resultFailed(e.message, status = HttpStatusCode.InternalServerError)
        }
    }


    delete("/clients/{id}") {
        val id = call.parameters["id"]?.toIntOrNull() ?: return@delete call.resultFailed(
            "ID inválido"
        )

        runCatching {
            repository.delete(id)
        }.onSuccess { deleted ->
            if (deleted) {
                call.resultSuccess(message = "Cliente removido")
            } else {
                call.resultFailed("Cliente não foi removido", status = HttpStatusCode.NotFound)
            }
        }.onFailure { e ->
            call.resultFailed(e.message, status = HttpStatusCode.InternalServerError)
        }
    }
}