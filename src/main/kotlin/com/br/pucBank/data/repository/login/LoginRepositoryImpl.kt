package com.br.pucBank.data.repository.login

import com.br.pucBank.data.repository.clients.ClientRepository
import com.br.pucBank.domain.clients.models.ClientResponse
import com.br.pucBank.domain.login.models.request.LoginRequest

class LoginRepositoryImpl(
    private val clientRepository: ClientRepository
) : LoginRepository {

    override suspend fun loginByAgency(loginRequest: LoginRequest): ClientResponse? {
        val client = clientRepository.findByAgencyAndAccount(loginRequest.agency, loginRequest.account) ?: return null

        return if (client.password == loginRequest.password) client else null
    }
}