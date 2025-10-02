package com.cinemapalace.database

import org.jetbrains.exposed.sql.Table

object ShowtimesTable : Table("Showtimes") {
    val id = varchar("id", 50)
    val theaterId = varchar("theaterId", 50).references(TheatersTable.id)
    val movieId = integer("movieId")
    val hall = varchar("hall", 100)
    val startTime = varchar("startTime", 30) // ISO string

    override val primaryKey = PrimaryKey(id)
}