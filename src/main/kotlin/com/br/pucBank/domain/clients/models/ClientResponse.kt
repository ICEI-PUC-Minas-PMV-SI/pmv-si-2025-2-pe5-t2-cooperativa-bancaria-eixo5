package com.br.pucBank.domain.clients.models

import kotlinx.serialization.Serializable

@Serializable
data class ClientResponse(
    val id: String,
    val name: String,
    val email: String,
    val account: Int,
    val agency: String,
    val password: Int
)