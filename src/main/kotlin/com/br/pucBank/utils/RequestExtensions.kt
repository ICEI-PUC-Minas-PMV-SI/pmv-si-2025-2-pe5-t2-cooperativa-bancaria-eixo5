package com.br.pucBank.utils

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*

suspend fun ApplicationCall.validateAuthentication(): JWTPrincipal {
    this.principal<JWTPrincipal>()?.let { principal ->
        return principal
    } ?: this.resultFailed(
        message = "Token inválido",
        status = HttpStatusCode.Unauthorized
    )

    throw IllegalStateException("Unreachable code")
}