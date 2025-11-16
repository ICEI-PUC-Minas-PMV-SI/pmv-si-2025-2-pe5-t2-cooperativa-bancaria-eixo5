package com.br.pucBank.data.database.models

import org.jetbrains.exposed.sql.Table

object  Clients: Table() {
    val id = integer("id").autoIncrement()
    val name = varchar("name", 200)
    val email = varchar("email", 200)
    override val primaryKey = PrimaryKey(id)
}
