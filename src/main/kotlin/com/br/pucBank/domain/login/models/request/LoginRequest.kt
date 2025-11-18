package com.br.pucBank.domain.login.models.request

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val agency: Int,
    val account: Int,
    val password: Int
)