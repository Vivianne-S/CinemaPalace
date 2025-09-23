package com.cinemapalace.repository

import com.cinemapalace.model.User
import java.util.concurrent.ConcurrentHashMap
import java.util.UUID

object UserRepository {
    private val users = ConcurrentHashMap<String, User>()

    fun register(name: String, email: String, password: String): User {
        val id = UUID.randomUUID().toString()
        val user = User(id, name, email, password)
        users[email] = user
        return user
    }

    fun login(email: String, password: String): User? {
        val user = users[email]
        return if (user != null && user.password == password) user else null
    }
}