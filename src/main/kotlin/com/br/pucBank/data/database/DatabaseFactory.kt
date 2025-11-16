package com.br.pucBank.data.database

import com.br.pucBank.data.database.models.Clients
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

const val BACKEND_IP = "3.226.219.79:3306"

object DatabaseFactory {
    fun init() {
        Database.connect(
            url = "jdbc:mysql://$BACKEND_IP/bank?useSSL=false&serverTimezone=UTC",
            driver = "com.mysql.cj.jdbc.Driver",
            user = "root",
            password = "Viniciusneves0703@"
        )

        transaction {
            SchemaUtils.create(
                Clients
            )
        }
    }
}