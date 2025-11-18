package com.br.pucBank.security

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import java.util.Date

class AuthenticationFactory(
    application: Application
) {
    private val config = application.environment.config

    private val jwtAudience = config.property("jwt.audience").getString()
    private val jwtIssuer = config.property("jwt.issuer").getString()
    private val jwtRealm = config.property("jwt.realm").getString()
    private val secret = config.property("jwt.secret").getString()

    fun configure(app: Application) {
        app.install(Authentication) {
            jwt {
                realm = jwtRealm
                verifier(
                    JWT
                        .require(Algorithm.HMAC256(secret))
                        .withIssuer(jwtIssuer)
                        .withAudience(jwtAudience)
                        .build()
                )
                validate { credential ->
                    val id = credential.payload.getClaim("id").asString()
                    if (id != null) JWTPrincipal(credential.payload) else null
                }
            }
        }
    }

    fun generateToken(clientId: String): String {
        val algorithm = Algorithm.HMAC256(secret)

        return JWT.create()
            .withIssuer(jwtIssuer)
            .withAudience(jwtAudience)
            .withClaim("id", clientId)
            .withExpiresAt(Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24)) // 24h
            .sign(algorithm)
    }
}