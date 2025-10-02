package com.cinemapalace.data.hall

import com.cinemapalace.database.HallsTable
import com.cinemapalace.database.SeatsTable
import com.cinemapalace.domain.models.Hall
import com.cinemapalace.domain.models.Seat
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

class HallRepository {

    private fun rowToHall(row: ResultRow) = Hall(
        id = row[HallsTable.id],
        theaterId = row[HallsTable.theaterId],
        name = row[HallsTable.name],
        rows = row[HallsTable.rows],
        cols = row[HallsTable.cols]
    )

    fun create(theaterId: String, name: String, rows: Int, cols: Int): Hall = transaction {
        val id = UUID.randomUUID().toString()
        HallsTable.insert {
            it[HallsTable.id] = id
            it[HallsTable.theaterId] = theaterId
            it[HallsTable.name] = name
            it[HallsTable.rows] = rows
            it[HallsTable.cols] = cols
        }

        // 🔹 Generera säten direkt
        for (r in 1..rows) {
            for (c in 1..cols) {
                val seatId = UUID.randomUUID().toString()
                SeatsTable.insert {
                    it[SeatsTable.id] = seatId
                    it[SeatsTable.hallId] = id
                    it[SeatsTable.row] = r
                    it[SeatsTable.col] = c
                    it[SeatsTable.type] = "NORMAL"
                    it[SeatsTable.status] = "FREE"
                }
            }
        }

        Hall(id, theaterId, name, rows, cols)
    }

    fun listAll(): List<Hall> = transaction {
        HallsTable.selectAll().map(::rowToHall)
    }

    fun listForTheater(theaterId: String): List<Hall> = transaction {
        HallsTable
            .select { HallsTable.theaterId eq theaterId }
            .map(::rowToHall)
    }
}