package com.cinemapalace.data.theater

import com.cinemapalace.database.TheatersTable
import com.cinemapalace.domain.models.TheaterDto
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

class TheaterRepository {
    fun list(): List<TheaterDto> = transaction {
        TheatersTable.selectAll().map { row ->
            TheaterDto(
                id = row[TheatersTable.id],
                name = row[TheatersTable.name],
                city = row[TheatersTable.city]
            )
        }
    }

    fun get(id: String): TheaterDto? = transaction {
        TheatersTable.select { TheatersTable.id eq id }.map { row ->
            TheaterDto(
                id = row[TheatersTable.id],
                name = row[TheatersTable.name],
                city = row[TheatersTable.city]
            )
        }.singleOrNull()
    }

    fun create(name: String, city: String): TheaterDto = transaction {
        val id = UUID.randomUUID().toString()
        TheatersTable.insert {
            it[TheatersTable.id] = id
            it[TheatersTable.name] = name
            it[TheatersTable.city] = city
        }
        TheaterDto(id, name, city)
    }
}