package com.cinemapalace.database

import org.jetbrains.exposed.sql.Table

object HallsTable : Table("halls") {
    val id = varchar("id", 50)
    val theaterId = varchar("theater_id", 50) references TheatersTable.id
    val name = varchar("name", 100) // Ex. "Salong 1"
    val rows = integer("rows")      // antal rader
    val cols = integer("cols")      // antal kolumner
    override val primaryKey = PrimaryKey(id)
}