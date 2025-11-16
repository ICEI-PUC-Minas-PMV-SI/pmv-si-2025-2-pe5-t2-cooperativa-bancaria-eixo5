package com.br.pucBank.data.repository.clients

import com.br.com.br.pucBank.utils.Logger
import com.br.pucBank.domain.dto.ClientDTO
import com.br.pucBank.domain.mappers.ClientDTOMapper
import com.br.pucBank.data.database.models.Clients
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

class ClientRepositoryImpl(
    private val clientDTOMapper: ClientDTOMapper
) : ClientRepository {

    override fun getAll(): List<ClientDTO> = transaction {
        Clients.selectAll().map { resultRow ->
            clientDTOMapper.toObject(resultRow)
        }
    }

    override fun getById(id: Int): ClientDTO? = transaction {
        Clients.select {
            Clients.id eq id
        }.map { resultRow ->
            clientDTOMapper.toObject(resultRow)
        }.singleOrNull()
    }

    override fun create(clientDto: ClientDTO): ClientDTO? = transaction {
        try {
            val id = Clients.insert {
                it[name] = clientDto.name
                it[email] = clientDto.email
            } get Clients.id

            getById(id)
        } catch (e: Exception) {
            Logger.e(e.message)

            null
        }
    }

    override fun update(id: Int, clientDto: ClientDTO): Boolean = transaction {
        Clients.update(
            where = { Clients.id eq id }
        ) {
            it[name] = clientDto.name
            it[email] = clientDto.email
        } > 0
    }

    override fun delete(id: Int): Boolean = transaction{
        Clients.deleteWhere {
            Clients.id eq id
        } > 0
    }
}