package com.cinemapalace.database

import org.jetbrains.exposed.sql.Table

object UsersTable : Table("Users") {
    val id = varchar("id", 50)
    val name = varchar("name", 100)
    val email = varchar("email", 100).uniqueIndex()
    val password = varchar("password", 200)

    override val primaryKey = PrimaryKey(id)
}