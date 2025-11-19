package com.br.pucBank.data.repository.clients

import com.br.pucBank.utils.Logger
import com.br.pucBank.domain.clients.models.ClientResponse
import com.br.pucBank.data.database.tables.Clients
import com.br.pucBank.domain.clients.mappers.ClientResponseMapper
import com.br.pucBank.domain.clients.models.ClientRequest
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

class ClientRepositoryImpl(
    private val clientResponseMapper: ClientResponseMapper
) : ClientRepository {

    override suspend fun getAll(): List<ClientResponse> = transaction {
        Clients.selectAll().map(clientResponseMapper::toObject)
    }

    override suspend fun getById(id: String): ClientResponse? = transaction {
        Clients.selectAll().where { Clients.id eq id }
            .map(clientResponseMapper::toObject)
            .singleOrNull()
    }

    override suspend fun create(clientRequest: ClientRequest): ClientResponse? = transaction {
        try {
            val clientId = UUID.randomUUID().toString()

            Clients.insert {
                it[id] = UUID.randomUUID().toString()
                it[name] = clientRequest.name ?: error("Name is required")
                it[email] = clientRequest.email ?: error("Email is required")
                it[agency] = clientRequest.agency ?: error("Agency is required")
                it[account] = clientRequest.account ?: error("Account is required")
                it[password] = clientRequest.password ?: error("Password is required")
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
        try {
            Clients.update({ Clients.id eq id }) { updateStatement ->
                clientRequest.name?.let { updateStatement[name] = it }
                clientRequest.email?.let { updateStatement[email] = it }
                clientRequest.password?.let { updateStatement[password] = it }
                clientRequest.agency?.let { updateStatement[agency] = it }
                clientRequest.account?.let { updateStatement[account] = it }
            } > 0
        } catch (e: Exception) {
            Logger.e(e.message)
            throw e
            false
        }
    }

    override suspend fun delete(id: String): Boolean = transaction {
        try {
            Clients.deleteWhere {
                Clients.id eq id
            } > 0
        } catch (e: Exception) {
            Logger.e(e.message)
            throw e
            false
        }
    }

    override suspend fun findByAgencyAndAccount(
        agency: String,
        account: Int
    ): ClientResponse? = transaction {
        Clients.selectAll().where { (Clients.agency eq agency) and (Clients.account eq account) }
            .map(clientResponseMapper::toObject)
            .singleOrNull()
    }
}