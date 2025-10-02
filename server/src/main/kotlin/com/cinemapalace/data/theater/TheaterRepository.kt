package com.cinemapalace.data.theater

import com.cinemapalace.database.TheatersTable
import com.cinemapalace.domain.models.Theater
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class TheaterRepository {
    private fun rowToTheater(row: ResultRow) = Theater(
        id = row[TheatersTable.id],
        name = row[TheatersTable.name],
        city = row[TheatersTable.city]
    )

    fun create(name: String, city: String): Theater = transaction {
        val id = UUID.randomUUID().toString()
        TheatersTable.insert {
            it[TheatersTable.id] = id
            it[TheatersTable.name] = name
            it[TheatersTable.city] = city
        }
        Theater(id, name, city)
    }

    fun list(): List<Theater> = transaction {
        TheatersTable.selectAll().map(::rowToTheater)
    }

    fun get(id: String): Theater? = transaction {
        TheatersTable
            .select { TheatersTable.id eq id }
            .map(::rowToTheater)
            .singleOrNull()
    }

    fun update(id: String, name: String, city: String): Theater? = transaction {
        val rowsUpdated = TheatersTable.update({ TheatersTable.id eq id }) {
            it[TheatersTable.name] = name
            it[TheatersTable.city] = city
        }
        if (rowsUpdated > 0) get(id) else null
    }

    fun delete(id: String): Boolean = transaction {
        TheatersTable.deleteWhere { TheatersTable.id eq id } > 0
    }
}