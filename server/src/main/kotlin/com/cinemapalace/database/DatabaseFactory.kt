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
            // Skapa alla tabeller vi använder i systemet
            SchemaUtils.create(
                UsersTable,
                BookingsTable,
                TheatersTable,   // 👈 viktig: avsluta med komma
                ShowtimesTable   // 👈 nu fungerar det
            )
        }

        println("✅ Database connected with Exposed: ${config.url}")
        println("✅ Users, Bookings, Theaters & Showtimes tables created/verified")
    }

    // kvar för kompatibilitet (dev)
    fun init() {
        init(DatabaseConfig("jdbc:sqlite:./cinemapalace.db"))
    }
}