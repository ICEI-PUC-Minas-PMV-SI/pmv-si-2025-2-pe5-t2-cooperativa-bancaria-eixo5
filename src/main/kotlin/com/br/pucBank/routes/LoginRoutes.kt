package com.br.pucBank.routes

import com.br.pucBank.data.repository.login.LoginRepository
import com.br.pucBank.domain.login.models.request.LoginRequest
import com.br.pucBank.domain.login.models.response.LoginResponse
import com.br.pucBank.security.AuthenticationFactory
import com.br.pucBank.utils.resultFailed
import com.br.pucBank.utils.resultSuccess
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import org.koin.ktor.ext.inject

fun Route.loginRoutes() {

    val repository: LoginRepository by inject()

    val authenticationFactory: AuthenticationFactory by inject()

    post("/auth/login") {

        val loginRequest = call.receive<LoginRequest>()

        val client = repository.loginByAgency(loginRequest) ?: return@post call.resultFailed(
            "Credenciais inválidas",
            status = HttpStatusCode.Unauthorized
        )

        val loginResponse = LoginResponse(
            token = authenticationFactory.generateToken(client.id),
            userId = client.id,
            name = client.name
        )

        call.resultSuccess(data = loginResponse)
    }
}