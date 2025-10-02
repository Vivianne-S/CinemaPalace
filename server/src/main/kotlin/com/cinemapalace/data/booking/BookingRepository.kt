package com.cinemapalace.data.booking

import com.cinemapalace.database.BookingsTable
import com.cinemapalace.domain.models.Booking
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class BookingRepository {

    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

    private fun rowToBooking(row: ResultRow): Booking =
        Booking(
            id = row[BookingsTable.id],
            userId = row[BookingsTable.userId],
            movieId = row[BookingsTable.movieId],
            seats = row[BookingsTable.seats].split(",").filter { it.isNotBlank() },
            showtime = row[BookingsTable.showtime],
            createdAt = row[BookingsTable.createdAt]
        )

    // ✅ Bytte namn till createBooking så att den matchar i bookingRoutes
    fun createBooking(userId: String, movieId: Int, seats: List<String>, showtime: String): Booking = transaction {
        val id = UUID.randomUUID().toString()
        val createdAt = LocalDateTime.now().format(formatter)
        val seatsStr = seats.joinToString(",")

        BookingsTable.insert {
            it[BookingsTable.id] = id
            it[BookingsTable.userId] = userId
            it[BookingsTable.movieId] = movieId
            it[BookingsTable.seats] = seatsStr
            it[BookingsTable.showtime] = showtime
            it[BookingsTable.createdAt] = createdAt
        }

        Booking(id, userId, movieId, seats, showtime, createdAt)
    }

    fun forUser(userId: String): List<Booking> = transaction {
        BookingsTable
            .select { BookingsTable.userId eq userId }
            .orderBy(BookingsTable.createdAt, SortOrder.DESC)
            .map(::rowToBooking)
    }

    fun delete(id: String, ownerId: String): Boolean = transaction {
        val rows = BookingsTable.deleteWhere { (BookingsTable.id eq id) and (BookingsTable.userId eq ownerId) }
        rows > 0
    }
}