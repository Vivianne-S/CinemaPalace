package com.cinemapalace.database

import org.jetbrains.exposed.sql.Table

object ShowtimesTable : Table("showtimes") {
    val id = varchar("id", 50)
    val theaterId = varchar("theater_id", 50) references TheatersTable.id
    val movieId = integer("movie_id")
    val hallId = varchar("hall_id", 50) references HallsTable.id
    val startTime = varchar("start_time", 30) // ISO string
    override val primaryKey = PrimaryKey(id)
}