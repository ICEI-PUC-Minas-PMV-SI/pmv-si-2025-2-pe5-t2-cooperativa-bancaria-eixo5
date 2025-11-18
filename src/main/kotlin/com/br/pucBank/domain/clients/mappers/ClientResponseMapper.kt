package com.br.pucBank.domain.clients.mappers

import com.br.pucBank.domain.clients.models.ClientResponse
import com.br.pucBank.data.database.tables.Clients
import com.br.pucBank.utils.Mapper
import org.jetbrains.exposed.sql.ResultRow

class ClientResponseMapper : Mapper<ResultRow, ClientResponse> {
    override fun toObject(fromObject: ResultRow) =
        ClientResponse(
            id = fromObject[Clients.id],
            name = fromObject[Clients.name],
            email = fromObject[Clients.email],
            account = fromObject[Clients.account],
            agency = fromObject[Clients.agency],
            password = fromObject[Clients.password]
        )
}