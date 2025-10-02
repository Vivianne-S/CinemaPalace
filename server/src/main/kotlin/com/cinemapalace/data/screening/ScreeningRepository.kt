package com.cinemapalace.data.screening

import com.cinemapalace.database.ScreeningsTable
import com.cinemapalace.domain.models.ScreeningDto
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDateTime
import java.util.*

private fun String.toSeatSet(): MutableSet<String> =
    if (isBlank()) mutableSetOf() else split(",").filter { it.isNotBlank() }.map { it.trim().uppercase() }.toMutableSet()

private fun Set<String>.toCsv(): String = this.joinToString(",")

class ScreeningRepository {

    fun create(movieId: Int, theaterId: String, startsAt: LocalDateTime, totalSeats: Int): ScreeningDto = transaction {
        val id = UUID.randomUUID().toString()
        ScreeningsTable.insert {
            it[ScreeningsTable.id] = id
            it[ScreeningsTable.movieId] = movieId
            it[ScreeningsTable.theaterId] = theaterId
            it[ScreeningsTable.startsAt] = startsAt
            it[ScreeningsTable.totalSeats] = totalSeats
            it[ScreeningsTable.bookedSeatsCsv] = ""
        }
        get(id)!!
    }

    fun get(id: String): ScreeningDto? = transaction {
        ScreeningsTable.select { ScreeningsTable.id eq id }.map { row ->
            val booked = row[ScreeningsTable.bookedSeatsCsv].toSeatSet().toList()
            ScreeningDto(
                id = row[ScreeningsTable.id],
                movieId = row[ScreeningsTable.movieId],
                theaterId = row[ScreeningsTable.theaterId],
                startsAt = row[ScreeningsTable.startsAt],
                totalSeats = row[ScreeningsTable.totalSeats],
                bookedSeats = booked,
                availableSeats = row[ScreeningsTable.totalSeats] - booked.size
            )
        }.singleOrNull()
    }

    fun listByMovie(movieId: Int): List<ScreeningDto> = transaction {
        ScreeningsTable.select { ScreeningsTable.movieId eq movieId }
            .orderBy(ScreeningsTable.startsAt to SortOrder.ASC)
            .map { row ->
                val booked = row[ScreeningsTable.bookedSeatsCsv].toSeatSet().toList()
                ScreeningDto(
                    id = row[ScreeningsTable.id],
                    movieId = row[ScreeningsTable.movieId],
                    theaterId = row[ScreeningsTable.theaterId],
                    startsAt = row[ScreeningsTable.startsAt],
                    totalSeats = row[ScreeningsTable.totalSeats],
                    bookedSeats = booked,
                    availableSeats = row[ScreeningsTable.totalSeats] - booked.size
                )
            }
    }

    fun listByTheater(theaterId: String): List<ScreeningDto> = transaction {
        ScreeningsTable.select { ScreeningsTable.theaterId eq theaterId }
            .orderBy(ScreeningsTable.startsAt to SortOrder.ASC)
            .map { row ->
                val booked = row[ScreeningsTable.bookedSeatsCsv].toSeatSet().toList()
                ScreeningDto(
                    id = row[ScreeningsTable.id],
                    movieId = row[ScreeningsTable.movieId],
                    theaterId = row[ScreeningsTable.theaterId],
                    startsAt = row[ScreeningsTable.startsAt],
                    totalSeats = row[ScreeningsTable.totalSeats],
                    bookedSeats = booked,
                    availableSeats = row[ScreeningsTable.totalSeats] - booked.size
                )
            }
    }

    /**
     * Markerar säten som bokade (utan att skapa booking record).
     * Används från BookingRepository efter validering.
     */
    fun appendBookedSeats(screeningId: String, newSeats: Set<String>) = transaction {
        val current = ScreeningsTable
            .slice(ScreeningsTable.bookedSeatsCsv, ScreeningsTable.totalSeats)
            .select { ScreeningsTable.id eq screeningId }
            .singleOrNull() ?: return@transaction

        val currentSet = current[ScreeningsTable.bookedSeatsCsv].toSeatSet()
        currentSet.addAll(newSeats.map { it.uppercase() })

        ScreeningsTable.update({ ScreeningsTable.id eq screeningId }) {
            it[bookedSeatsCsv] = currentSet.toCsv()
        }
    }

    fun seatsAvailable(screeningId: String, seats: Set<String>): Boolean = transaction {
        val row = ScreeningsTable
            .slice(ScreeningsTable.bookedSeatsCsv, ScreeningsTable.totalSeats)
            .select { ScreeningsTable.id eq screeningId }
            .singleOrNull() ?: return@transaction false

        val booked = row[ScreeningsTable.bookedSeatsCsv].toSeatSet()
        seats.map { it.uppercase() }.none { it in booked }
    }
}