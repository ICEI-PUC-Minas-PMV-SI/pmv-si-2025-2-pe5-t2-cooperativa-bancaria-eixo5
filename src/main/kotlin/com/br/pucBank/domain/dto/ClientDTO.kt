package com.br.pucBank.domain.dto

import kotlinx.serialization.Serializable

@Serializable
data class ClientDTO(
    val id: Int? = null, val name: String, val email: String
)
