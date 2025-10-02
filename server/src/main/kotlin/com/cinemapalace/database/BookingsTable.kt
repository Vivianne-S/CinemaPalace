package com.cinemapalace.database

import org.jetbrains.exposed.sql.Table

object BookingsTable : Table("Bookings") {
    val id = varchar("id", 50)
    val userId = varchar("userId", 50).references(UsersTable.id)
    val movieId = integer("movieId")
    val seats = text("seats") // kommaseparerade platser: "A1,A2"
    val showtime = varchar("showtime", 30) // ISO-8601 tid som text
    val createdAt = varchar("createdAt", 30)

    override val primaryKey = PrimaryKey(id)
}