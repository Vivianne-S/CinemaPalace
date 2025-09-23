package com.cinemapalace.database

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {
    fun init() {
        Database.connect("jdbc:sqlite:cinema.db", driver = "org.sqlite.JDBC")

        transaction {
            SchemaUtils.create(UsersTable)
        }
    }
}