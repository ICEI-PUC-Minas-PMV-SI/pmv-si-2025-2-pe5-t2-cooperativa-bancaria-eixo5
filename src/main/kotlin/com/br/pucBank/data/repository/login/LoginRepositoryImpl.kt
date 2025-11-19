package com.br.pucBank.data.repository.login

import com.br.pucBank.data.repository.clients.ClientRepository
import com.br.pucBank.domain.clients.models.ClientResponse
import com.br.pucBank.domain.login.models.LoginRequest

class LoginRepositoryImpl(
    private val clientRepository: ClientRepository
) : LoginRepository {

    override suspend fun loginByAgencyAndAccount(loginRequest: LoginRequest): ClientResponse? =
        clientRepository.findByAgencyAndAccount(loginRequest.agency, loginRequest.account, loginRequest.password)
}