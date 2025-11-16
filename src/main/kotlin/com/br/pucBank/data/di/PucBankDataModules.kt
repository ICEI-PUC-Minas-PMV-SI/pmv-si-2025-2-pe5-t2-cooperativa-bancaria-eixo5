package com.br.pucBank.data.di

import com.br.pucBank.domain.mappers.ClientDTOMapper
import com.br.pucBank.data.repository.clients.ClientRepository
import com.br.pucBank.data.repository.clients.ClientRepositoryImpl
import org.koin.dsl.module

val pucBankDataModules = module {
    factory { ClientDTOMapper() }

    factory<ClientRepository> { ClientRepositoryImpl(get()) }
}