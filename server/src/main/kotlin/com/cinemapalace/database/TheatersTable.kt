package com.cinemapalace.database

import org.jetbrains.exposed.sql.Table

object TheatersTable : Table("Theaters") {
    val id = varchar("id", 50)
    val name = varchar("name", 120)
    val city = varchar("city", 120)

    override val primaryKey = PrimaryKey(id)
}