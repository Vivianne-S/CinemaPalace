package com.cinemapalace.data.seat

import com.cinemapalace.database.SeatsTable
import com.cinemapalace.domain.models.Seat
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class SeatRepository {
    private fun rowToSeat(row: ResultRow) = Seat(
        id = row[SeatsTable.id],
        hallId = row[SeatsTable.hallId],
        row = row[SeatsTable.row],
        col = row[SeatsTable.col],
        type = row[SeatsTable.type],
        status = row[SeatsTable.status]
    )

    fun listForHall(hallId: String): List<Seat> = transaction {
        SeatsTable
            .select { SeatsTable.hallId eq hallId }
            .orderBy(SeatsTable.row to SortOrder.ASC, SeatsTable.col to SortOrder.ASC)
            .map(::rowToSeat)
    }
}