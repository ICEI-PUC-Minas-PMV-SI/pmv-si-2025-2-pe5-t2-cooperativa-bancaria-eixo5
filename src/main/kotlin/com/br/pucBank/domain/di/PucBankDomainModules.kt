package com.br.pucBank.domain.di

import com.br.pucBank.domain.clients.mappers.ClientResponseMapper
import org.koin.dsl.module

val pucBankDomainModules = module {
    factory { ClientResponseMapper() }
}