package com.cinemapalace.database

import org.jetbrains.exposed.sql.Table

object TheatersTable : Table("theaters") {
    val id = varchar("id", 50)
    val name = varchar("name", 100)
    val city = varchar("city", 100)
    override val primaryKey = PrimaryKey(id)
}