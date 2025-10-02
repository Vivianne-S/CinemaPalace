package com.cinemapalace.database

import org.jetbrains.exposed.sql.Table

object SeatsTable : Table("seats") {
    val id = varchar("id", 50)
    val hallId = varchar("hall_id", 50) references HallsTable.id
    val row = integer("row")
    val col = integer("col")
    val type = varchar("type", 50)   // NORMAL, WHEELCHAIR, VIP
    val status = varchar("status", 50) // FREE, RESERVED, BOOKED
    override val primaryKey = PrimaryKey(id)
}