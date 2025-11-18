package com.br.pucBank.data.database.models

import org.jetbrains.exposed.sql.Table

object Clients : Table() {
    val id = varchar("id", 250)
    val account = integer("account").uniqueIndex()
    val agency = integer("agency")
    val name = varchar("name", 200)
    val email = varchar("email", 200)
    val password = integer("password")
    override val primaryKey = PrimaryKey(id)
}