package com.br.pucBank.security

import io.ktor.server.application.Application
import org.koin.core.module.Module
import org.koin.dsl.module

fun injectPucBankSecurityModules(application: Application): Module {
    return module {
        single { AuthenticationFactory(application) }
    }
}