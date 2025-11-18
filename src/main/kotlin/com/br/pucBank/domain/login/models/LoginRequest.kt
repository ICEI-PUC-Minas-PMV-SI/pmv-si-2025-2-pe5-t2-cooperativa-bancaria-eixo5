package com.br.pucBank.domain.login.models

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val agency: String,
    val account: Int,
    val password: Int
)