package com.cinemapalace.data.auth

import com.cinemapalace.database.UsersTable
import com.cinemapalace.domain.models.User
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.mindrot.jbcrypt.BCrypt
import java.util.*

class UserRepository {
    fun findByEmail(email: String): User? = transaction {
        UsersTable.select { UsersTable.email eq email }
            .map { row ->
                User(
                    id = row[UsersTable.id],
                    name = row[UsersTable.name],
                    email = row[UsersTable.email],
                    password = row[UsersTable.password]
                )
            }
            .singleOrNull()
    }

    fun findById(id: String): User? = transaction {
        UsersTable.select { UsersTable.id eq id }
            .map { row ->
                User(
                    id = row[UsersTable.id],
                    name = row[UsersTable.name],
                    email = row[UsersTable.email],
                    password = row[UsersTable.password]
                )
            }
            .singleOrNull()
    }

    fun createUser(name: String, email: String, password: String): User = transaction {
        val hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt())
        val userId = UUID.randomUUID().toString()

        UsersTable.insert { insert ->
            insert[UsersTable.id] = userId
            insert[UsersTable.name] = name
            insert[UsersTable.email] = email
            insert[UsersTable.password] = hashedPassword
        }

        User(userId, name, email, hashedPassword)
    }

    fun validateUser(email: String, password: String): User? {
        val user = findByEmail(email)
        return if (user != null && BCrypt.checkpw(password, user.password)) {
            user
        } else null
    }
}