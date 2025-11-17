package com.br.pucBank.utils

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.http.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.encodeToJsonElement
import javax.swing.UIManager.put

suspend fun ApplicationCall.resultSuccess(data: Any? = null, message: String? = null, status: HttpStatusCode = HttpStatusCode.OK) {
    this.respond(
        status,
        buildJsonObject {
            put("success", true)
            message?.let { put("message", it) }
            data?.let { put("data", Json.encodeToJsonElement(it)) }
        }
    )
}

suspend fun ApplicationCall.resultFailed(error: Any? = null, message: String? = null, status: HttpStatusCode = HttpStatusCode.BadRequest) {
    this.respond(
        status,
        buildJsonObject {
            put("success", false)
            message?.let { put("message", it) }
            error?.let { put("error", it) }
        }
    )
}