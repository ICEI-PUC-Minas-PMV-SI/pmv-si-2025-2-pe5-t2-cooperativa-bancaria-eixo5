package com.br.pucBank.data.repository.login

import com.br.pucBank.domain.clients.models.ClientResponse
import com.br.pucBank.domain.login.models.LoginRequest

interface LoginRepository {
    suspend fun loginByAgencyAndAccount(loginRequest: LoginRequest): ClientResponse?
}