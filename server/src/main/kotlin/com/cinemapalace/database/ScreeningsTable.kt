package com.cinemapalace.database

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.datetime

object ScreeningsTable : Table("Screenings") {
    val id = varchar("id", 50)
    val movieId = integer("movieId")             // TMDB movie id
    val theaterId = varchar("theaterId", 50).references(TheatersTable.id)
    val startsAt = datetime("startsAt")          // LocalDateTime
    val totalSeats = integer("totalSeats")       // exempel: 60, 80 osv
    val bookedSeatsCsv = text("bookedSeatsCsv").default("") // ex: "A1,A2,B5"

    override val primaryKey = PrimaryKey(id)
}