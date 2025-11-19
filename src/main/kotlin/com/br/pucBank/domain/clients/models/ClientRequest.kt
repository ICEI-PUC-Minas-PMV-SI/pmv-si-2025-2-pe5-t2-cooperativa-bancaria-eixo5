package com.br.pucBank.domain.clients.models

import kotlinx.serialization.Serializable

@Serializable
data class ClientRequest(
    val name: String? = null,
    val email: String? = null,
    val account: Int? = null,
    val agency: String? = null,
    val password: Int? = null
)