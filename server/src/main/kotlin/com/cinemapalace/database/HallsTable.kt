package com.cinemapalace.database

import org.jetbrains.exposed.sql.Table

object HallsTable : Table("halls") {
    val id = varchar("id", 50)
    val theaterId = varchar("theater_id", 50)
    val name = varchar("name", 100)

    // 👇 Dessa två behövs eftersom HallRepository refererar till dem
    val rows = integer("rows")
    val cols = integer("cols")

    val totalSeats = integer("total_seats")

    override val primaryKey = PrimaryKey(id)
}