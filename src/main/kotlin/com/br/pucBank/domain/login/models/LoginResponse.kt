package com.br.pucBank.domain.login.models

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    val token: String,
    val userId: String,
    val name: String
)