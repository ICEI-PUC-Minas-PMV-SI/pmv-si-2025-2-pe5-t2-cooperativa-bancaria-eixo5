package com.br.pucBank.data.repository.clients

import com.br.pucBank.utils.Logger
import com.br.pucBank.domain.clients.models.ClientResponse
import com.br.pucBank.data.database.tables.Clients
import com.br.pucBank.domain.clients.mappers.ClientResponseMapper
import com.br.pucBank.domain.clients.models.ClientRequest
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

class ClientRepositoryImpl(
    private val clientResponseMapper: ClientResponseMapper
) : ClientRepository {

    override suspend fun getAll(): List<ClientResponse> = transaction {
        Clients.selectAll().map(clientResponseMapper::toObject)
    }

    override suspend fun getById(id: String): ClientResponse? = transaction {
        Clients.select { Clients.id eq id }
            .map(clientResponseMapper::toObject)
            .singleOrNull()
    }

    override suspend fun create(clientRequest: ClientRequest): ClientResponse? = transaction {
        try {
            val clientId = UUID.randomUUID().toString()

            Clients.insert {
                it[id] = clientId
                it[name] = clientRequest.name
                it[email] = clientRequest.email
                it[agency] = clientRequest.agency
                it[account] = clientRequest.account
                it[password] = clientRequest.password
            }

            runBlocking {
                getById(clientId)
            }
        } catch (e: Exception) {
            Logger.e(e.message)
            null
        }
    }

    override suspend fun update(id: String, clientRequest: ClientRequest): Boolean = transaction {
        Clients.update(
            { Clients.id eq id }
        ) {
            it[name] = clientRequest.name
            it[email] = clientRequest.email
            it[password] = clientRequest.password
        } > 0
    }

    override suspend fun delete(id: String): Boolean = transaction {
        Clients.deleteWhere {
            Clients.id eq id
        } > 0
    }

    override suspend fun findByAgencyAndAccount(
        agency: Int,
        account: Int
    ): ClientResponse? = transaction {
        Clients.select {
            (Clients.agency eq agency) and (Clients.account eq account)
        }.map(clientResponseMapper::toObject)
            .singleOrNull()
    }
}