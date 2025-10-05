package com.cinemapalace.data.showtime

import com.cinemapalace.database.ShowtimesTable
import com.cinemapalace.domain.models.Showtime
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import java.util.*

class ShowtimesRepository {
    private fun rowToShowtime(row: ResultRow) = Showtime(
        id = row[ShowtimesTable.id],
        theaterId = row[ShowtimesTable.theaterId],
        movieId = row[ShowtimesTable.movieId],
        hallId = row[ShowtimesTable.hallId],   // ✅ hallId istället för hall
        startTime = row[ShowtimesTable.startTime]
    )

    fun create(theaterId: String, movieId: Int, hallId: String, startTime: String): Showtime = transaction {
        val id = UUID.randomUUID().toString()
        ShowtimesTable.insert {
            it[ShowtimesTable.id] = id
            it[ShowtimesTable.theaterId] = theaterId
            it[ShowtimesTable.movieId] = movieId
            it[ShowtimesTable.hallId] = hallId   // ✅
            it[ShowtimesTable.startTime] = startTime
        }
        Showtime(id, theaterId, movieId, hallId, startTime)
    }

    fun listAll(): List<Showtime> = transaction {
        ShowtimesTable
            .selectAll()
            .orderBy(ShowtimesTable.startTime, SortOrder.ASC)
            .map(::rowToShowtime)
    }

    fun get(id: String): Showtime? = transaction {
        ShowtimesTable
            .select { ShowtimesTable.id eq id }
            .map(::rowToShowtime)
            .singleOrNull()
    }

    fun update(id: String, theaterId: String, movieId: Int, hallId: String, startTime: String): Showtime? = transaction {
        val updated = ShowtimesTable.update({ ShowtimesTable.id eq id }) {
            it[ShowtimesTable.theaterId] = theaterId
            it[ShowtimesTable.movieId] = movieId
            it[ShowtimesTable.hallId] = hallId   // ✅
            it[ShowtimesTable.startTime] = startTime
        }
        if (updated > 0) {
            ShowtimesTable
                .select { ShowtimesTable.id eq id }
                .map(::rowToShowtime)
                .singleOrNull()
        } else null
    }

    fun getByTheater(theaterId: String): List<Showtime> = transaction {
        ShowtimesTable
            .select { ShowtimesTable.theaterId eq theaterId }
            .orderBy(ShowtimesTable.startTime, SortOrder.ASC)
            .map(::rowToShowtime)
    }

    fun delete(id: String): Boolean = transaction {
        ShowtimesTable.deleteWhere { ShowtimesTable.id eq id } > 0
    }
    // 🔹 Ny metod: lista alla visningar grupperade per biograf
    fun getGroupedShowtimes(): List<Map<String, Any>> = transaction {
        (com.cinemapalace.database.ShowtimesTable innerJoin com.cinemapalace.database.TheatersTable innerJoin com.cinemapalace.database.HallsTable)
            .selectAll()
            .orderBy(com.cinemapalace.database.ShowtimesTable.startTime, SortOrder.ASC)
            .map {
                mapOf(
                    "theaterName" to it[com.cinemapalace.database.TheatersTable.name],
                    "hallName" to it[com.cinemapalace.database.HallsTable.name],
                    "totalSeats" to it[com.cinemapalace.database.HallsTable.totalSeats],
                    "availableSeats" to 250, // 💡 placeholder tills SeatBookings är kopplat
                    "time" to it[com.cinemapalace.database.ShowtimesTable.startTime]
                )
            }
    }
}