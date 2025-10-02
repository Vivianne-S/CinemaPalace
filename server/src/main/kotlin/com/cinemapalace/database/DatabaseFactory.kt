package com.cinemapalace.database

import com.cinemapalace.config.DatabaseConfig
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {
    fun init(config: DatabaseConfig) {
        Database.connect(
            url = config.url,
            driver = "org.sqlite.JDBC"
        )

        transaction {
            // Skapa alla tabeller i rätt ordning
            SchemaUtils.create(
                UsersTable,
                TheatersTable,
                ShowtimesTable,
                HallsTable,
                SeatsTable,
                SeatBookingsTable
            )
        }

        println("✅ Database connected with Exposed: ${config.url}")
        println("✅ Users, Theaters, Showtimes, Halls, Seats & SeatBookings tables created/verified")
    }

    // fallback för dev
    fun init() {
        init(DatabaseConfig("jdbc:sqlite:./cinemapalace.db"))
    }
}