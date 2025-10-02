package com.cinemapalace.database

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.datetime

object BookingsTable : Table("Bookings") {
    val id = varchar("id", 50)
    val userId = varchar("userId", 50).references(UsersTable.id)
    val screeningId = varchar("screeningId", 50).references(ScreeningsTable.id)
    val seatsCsv = text("seatsCsv")            // ex: "A1,A2"
    val createdAt = datetime("createdAt")

    override val primaryKey = PrimaryKey(id)
}