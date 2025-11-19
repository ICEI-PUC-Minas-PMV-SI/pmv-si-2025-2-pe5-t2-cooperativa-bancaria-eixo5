package com.br.pucBank.data.repository.clients

import com.br.pucBank.domain.clients.models.ClientRequest
import com.br.pucBank.domain.clients.models.ClientResponse

interface ClientRepository {
    suspend fun getAll(): List<ClientResponse>

    suspend fun getById(id: String): ClientResponse?

    suspend fun create(clientRequest: ClientRequest): ClientResponse?

    suspend fun update(id: String, clientRequest: ClientRequest): ClientResponse?

    suspend fun delete(id: String): Boolean

    suspend fun findByAgencyAndAccount(agency: String, account: Int, password: Int): ClientResponse?
}