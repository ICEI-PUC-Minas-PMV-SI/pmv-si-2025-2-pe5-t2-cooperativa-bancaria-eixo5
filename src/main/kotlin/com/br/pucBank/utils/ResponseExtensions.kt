package com.br.pucBank.utils

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.http.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.encodeToJsonElement
import javax.swing.UIManager.put

suspend inline fun <reified T> ApplicationCall.resultSuccess(
    data: T? = null,
    message: String? = null,
    status: HttpStatusCode = HttpStatusCode.OK
) {
    this.respond(
        status,
        buildJsonObject {
            message?.let { put("message", it) }
            data?.let { put("data", Json.encodeToJsonElement(data)) }
        }
    )
}

suspend fun ApplicationCall.resultSuccess(
    message: String? = null,
    status: HttpStatusCode = HttpStatusCode.OK
) {
    this.respond(
        status,
        buildJsonObject {
            message?.let { put("message", it) }
        }
    )
}

suspend inline fun <reified T> ApplicationCall.resultFailed(
    error: T? = null,
    message: String? = null,
    status: HttpStatusCode = HttpStatusCode.BadRequest
) {
    this.respond(
        status,
        buildJsonObject {
            message?.let { put("message", it) }
            error?.let { put("error", Json.encodeToJsonElement(error)) }
        }
    )
}
