package com.br.pucBank.routes

import com.br.com.br.pucBank.utils.Logger
import com.br.pucBank.data.repository.clients.ClientRepository
import com.br.pucBank.domain.dto.ClientDTO
import io.ktor.http.*
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.json.put
import org.koin.ktor.ext.inject

fun Route.clientRoutes() {
    val repository: ClientRepository by inject()

    get("/clients") {
        try {
            Logger.i { "GET - /clients" }

            val clients = repository.getAll()

            call.respond(
                HttpStatusCode.OK,
                buildJsonObject {
                    put("success", true)
                    put("data", Json.encodeToJsonElement(clients))
                }
            )

            Logger.i { "GET - /clients responding with ${Json.encodeToJsonElement(clients)}" }
        } catch (e: Exception) {
            Logger.i { "GET - /clients error: ${e.message}" }
            call.respond(
                HttpStatusCode.InternalServerError,
                buildJsonObject {
                    put("success", false)
                    put("error", e.message ?: "Erro desconhecido")
                }
            )
        }
    }

    get("/clients/{id}") {
        try {
            val id = call.parameters["id"]?.toIntOrNull()
                ?: return@get call.respond(
                    HttpStatusCode.BadRequest,
                    buildJsonObject {
                        put("success", false)
                        put("error", "ID inválido")
                    }
                )

            val client = repository.getById(id)

            if (client != null) {
                call.respond(
                    HttpStatusCode.OK,
                    buildJsonObject {
                        put("success", true)
                        put("data", Json.encodeToJsonElement(client))
                    }
                )
            } else {
                call.respond(
                    HttpStatusCode.NotFound,
                    buildJsonObject {
                        put("success", false)
                        put("error", "Cliente não encontrado")
                    }
                )
            }
        } catch (e: Exception) {
            call.respond(
                HttpStatusCode.InternalServerError,
                buildJsonObject {
                    put("success", false)
                    put("error", e.message ?: "Erro desconhecido")
                }
            )
        }
    }

    post("/clients") {
        try {
            val clientDTO = call.receive<ClientDTO>()
            val result = repository.create(clientDTO)

            if (result != null) {
                call.respond(
                    HttpStatusCode.Created,
                    buildJsonObject {
                        put("success", true)
                        put("data", Json.encodeToJsonElement(result))
                    }
                )
            } else {
                call.respond(
                    HttpStatusCode.BadRequest,
                    buildJsonObject {
                        put("success", false)
                        put("error", "Cliente não foi criado")
                    }
                )
            }
        } catch (e: Exception) {
            call.respond(
                HttpStatusCode.InternalServerError,
                buildJsonObject {
                    put("success", false)
                    put("error", e.message ?: "Erro desconhecido")
                }
            )
        }
    }

    put("/clients/{id}") {
        try {
            val id = call.parameters["id"]?.toIntOrNull()
                ?: return@put call.respond(
                    HttpStatusCode.BadRequest,
                    buildJsonObject {
                        put("success", false)
                        put("error", "ID inválido")
                    }
                )

            val clientDTO = call.receive<ClientDTO>()
            val result = repository.update(id, clientDTO)

            call.respond(
                HttpStatusCode.OK,
                buildJsonObject {
                    put("success", true)
                    put("data", Json.encodeToJsonElement(result))
                }
            )
        } catch (e: Exception) {
            call.respond(
                HttpStatusCode.InternalServerError,
                buildJsonObject {
                    put("success", false)
                    put("error", e.message ?: "Erro desconhecido")
                }
            )
        }
    }

    delete("/clients/{id}") {
        try {
            val id = call.parameters["id"]?.toIntOrNull()
                ?: return@delete call.respond(
                    HttpStatusCode.BadRequest,
                    buildJsonObject {
                        put("success", false)
                        put("error", "ID inválido")
                    }
                )

            val deleted = repository.delete(id)

            if (deleted) {
                call.respond(
                    HttpStatusCode.OK,
                    buildJsonObject {
                        put("success", true)
                        put("message", "Cliente removido")
                    }
                )
            } else {
                call.respond(
                    HttpStatusCode.NotFound,
                    buildJsonObject {
                        put("success", false)
                        put("error", "Cliente não foi removido")
                    }
                )
            }
        } catch (e: Exception) {
            call.respond(
                HttpStatusCode.InternalServerError,
                buildJsonObject {
                    put("success", false)
                    put("error", e.message ?: "Erro desconhecido")
                }
            )
        }
    }
}