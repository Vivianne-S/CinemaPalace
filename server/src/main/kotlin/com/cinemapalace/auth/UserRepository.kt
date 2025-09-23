package com.cinemapalace.auth

import com.cinemapalace.model.User
import com.cinemapalace.database.UsersTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.mindrot.jbcrypt.BCrypt
import java.util.*

class UserRepository {

    // Skapa ny användare
    fun createUser(name: String, email: String, password: String): User {
        val hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt())
        val userId = UUID.randomUUID().toString()

        transaction {
            UsersTable.insert {
                it[id] = userId
                it[UsersTable.name] = name
                it[UsersTable.email] = email
                it[UsersTable.password] = hashedPassword
            }
        }

        return User(userId, name, email, hashedPassword)
    }

    // Hitta användare via email
    fun findByEmail(email: String): User? {
        return transaction {
            UsersTable.selectAll()
                .where { UsersTable.email eq email }
                .map {
                    User(
                        it[UsersTable.id],
                        it[UsersTable.name],
                        it[UsersTable.email],
                        it[UsersTable.password]
                    )
                }
                .singleOrNull()
        }
    }

    // Validera login
    fun validateUser(email: String, password: String): User? {
        val user = findByEmail(email)
        return if (user != null && BCrypt.checkpw(password, user.password)) user else null
    }
}