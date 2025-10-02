package com.cinemapalace.data.booking

import com.cinemapalace.database.BookingsTable
import com.cinemapalace.domain.models.BookingDto
import com.cinemapalace.data.screening.ScreeningRepository
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDateTime
import java.util.*

private fun List<String>.toCsv(): String = this.joinToString(",")

class BookingRepository(
    private val screeningRepo: ScreeningRepository = ScreeningRepository()
) {
    fun create(userId: String, screeningId: String, seats: List<String>): BookingDto {
        require(seats.isNotEmpty()) { "No seats provided" }
        val seatSet = seats.map { it.trim().uppercase() }.toSet()

        // Validations + update screening
        if (!screeningRepo.seatsAvailable(screeningId, seatSet)) {
            throw IllegalStateException("One or more seats are already booked")
        }
        screeningRepo.appendBookedSeats(screeningId, seatSet)

        // Create booking
        return transaction {
            val id = UUID.randomUUID().toString()
            val now = LocalDateTime.now()
            BookingsTable.insert {
                it[BookingsTable.id] = id
                it[BookingsTable.userId] = userId
                it[BookingsTable.screeningId] = screeningId
                it[BookingsTable.seatsCsv] = seats.toCsv()
                it[BookingsTable.createdAt] = now
            }
            BookingDto(
                id = id,
                screeningId = screeningId,
                userId = userId,
                seats = seats,
                createdAt = now
            )
        }
    }

    fun listByUser(userId: String): List<BookingDto> = transaction {
        BookingsTable.select { BookingsTable.userId eq userId }
            .orderBy(BookingsTable.createdAt to SortOrder.DESC)
            .map { row ->
                BookingDto(
                    id = row[BookingsTable.id],
                    screeningId = row[BookingsTable.screeningId],
                    userId = row[BookingsTable.userId],
                    seats = row[BookingsTable.seatsCsv].split(",").filter { it.isNotBlank() },
                    createdAt = row[BookingsTable.createdAt]
                )
            }
    }
}