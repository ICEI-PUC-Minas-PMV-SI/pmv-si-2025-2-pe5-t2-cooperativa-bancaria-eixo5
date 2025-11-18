package com.br.pucBank.domain.clients.models

import kotlinx.serialization.Serializable

@Serializable
data class ClientRequest(
    val name: String,
    val email: String,
    val account: Int,
    val agency: Int,
    val password: Int
)