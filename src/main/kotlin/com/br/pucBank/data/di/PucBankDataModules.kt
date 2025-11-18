package com.br.pucBank.data.di

import com.br.pucBank.data.repository.clients.ClientRepository
import com.br.pucBank.data.repository.clients.ClientRepositoryImpl
import com.br.pucBank.data.repository.login.LoginRepository
import com.br.pucBank.data.repository.login.LoginRepositoryImpl
import org.koin.dsl.module

val pucBankDataModules = module {
    factory<LoginRepository> {  LoginRepositoryImpl(get()) }
    factory<ClientRepository> { ClientRepositoryImpl(get()) }
}