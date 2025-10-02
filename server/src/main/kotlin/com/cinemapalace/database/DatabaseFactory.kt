package com.cinemapalace.database

import com.cinemapalace.config.DatabaseConfig
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {
    fun init(config: DatabaseConfig) {
        // Anslut till databasen med Exposed
        Database.connect(
            url = config.url,
            driver = "org.sqlite.JDBC"
        )

        // Skapa tabeller (just nu bara Users, men fler kommer: Cinemas, Movies, Screenings, Bookings)
        transaction {
            SchemaUtils.create(UsersTable)
        }

        println("✅ Database connected with Exposed: ${config.url}")
        println("✅ Users table created/verified")
    }

    // 🔹 fallback om ingen config skickas
    fun init() {
        init(DatabaseConfig("jdbc:sqlite:./cinemapalace.db"))
    }
}