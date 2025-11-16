package com.br.pucBank.data.repository.clients

import com.br.pucBank.domain.dto.ClientDTO

interface ClientRepository {

    fun getAll(): List<ClientDTO>

    fun getById(
        id: Int
    ): ClientDTO?

    fun create(
        clientDto: ClientDTO
    ): ClientDTO?

    fun update(
        id: Int,
        clientDto: ClientDTO
    ): Boolean

    fun delete(
        id: Int
    ): Boolean

}