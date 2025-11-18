package com.br.pucBank.security

import org.koin.dsl.module

val pucBankSecurityModules = module {
    single { this@module }

    single { AuthenticationFactory(get()) }
}