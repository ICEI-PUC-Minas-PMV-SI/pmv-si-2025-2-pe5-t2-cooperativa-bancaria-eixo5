package com.br.pucBank.data.repository.login

import com.br.pucBank.domain.clients.models.ClientResponse
import com.br.pucBank.domain.login.models.request.LoginRequest

interface LoginRepository {
    suspend fun loginByAgency(loginRequest: LoginRequest): ClientResponse?
}