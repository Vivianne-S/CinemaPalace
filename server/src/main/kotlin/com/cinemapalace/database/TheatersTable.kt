package com.cinemapalace.database

import org.jetbrains.exposed.sql.Table

object TheatersTable : Table("Theaters") {
    val id = varchar("id", 50)
    val name = varchar("name", 255)
    val city = varchar("city", 255)

    override val primaryKey = PrimaryKey(id)
}