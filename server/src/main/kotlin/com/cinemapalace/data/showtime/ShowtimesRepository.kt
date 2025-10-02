package com.cinemapalace.data.showtime

import com.cinemapalace.database.ShowtimesTable
import com.cinemapalace.domain.models.Showtime
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class ShowtimesRepository {
    private fun rowToShowtime(row: ResultRow) = Showtime(
        id = row[ShowtimesTable.id],
        theaterId = row[ShowtimesTable.theaterId],
        movieId = row[ShowtimesTable.movieId],
        hall = row[ShowtimesTable.hall],
        startTime = row[ShowtimesTable.startTime]
    )

    fun create(theaterId: String, movieId: Int, hall: String, startTime: String): Showtime = transaction {
        val id = UUID.randomUUID().toString()
        ShowtimesTable.insert {
            it[ShowtimesTable.id] = id
            it[ShowtimesTable.theaterId] = theaterId
            it[ShowtimesTable.movieId] = movieId
            it[ShowtimesTable.hall] = hall
            it[ShowtimesTable.startTime] = startTime
        }
        Showtime(id, theaterId, movieId, hall, startTime)
    }

    fun forTheater(theaterId: String): List<Showtime> = transaction {
        ShowtimesTable
            .select { ShowtimesTable.theaterId eq theaterId }
            .orderBy(ShowtimesTable.startTime, SortOrder.ASC)
            .map(::rowToShowtime)
    }

    fun listAll(): List<Showtime> = transaction {
        ShowtimesTable
            .selectAll()
            .orderBy(ShowtimesTable.startTime, SortOrder.ASC)
            .map(::rowToShowtime)
    }

    fun delete(id: String): Boolean = transaction {
        val rowsDeleted = ShowtimesTable.deleteWhere { ShowtimesTable.id eq id }
        rowsDeleted > 0
    }
}