package com.br.pucBank.domain.mappers

import com.br.pucBank.domain.dto.ClientDTO
import com.br.pucBank.data.database.models.Clients
import com.br.pucBank.utils.Mapper
import org.jetbrains.exposed.sql.ResultRow

class ClientDTOMapper : Mapper<ResultRow, ClientDTO> {
    override fun toObject(fromObject: ResultRow) =
        ClientDTO(
            id = fromObject[Clients.id],
            name = fromObject[Clients.name],
            email = fromObject[Clients.email]
        )
}