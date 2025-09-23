package com.cinemapalace

import com.cinemapalace.auth.authRoutes
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.jackson.jackson
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.routing.*
import io.ktor.server.testing.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class AuthRoutesTest {

    @Test
    fun testRegisterAndLogin() = testApplication {
        application {
            install(ContentNegotiation) {
                jackson()
            }
            routing {
                authRoutes()
            }
        }

        // Register user
        val registerResponse = client.post("/register") {
            contentType(ContentType.Application.Json)
            setBody("""{"name":"Test","email":"test@example.com","password":"secret"}""")
        }
        println("➡️ Register response: ${registerResponse.status}, body=${registerResponse.bodyAsText()}")
        assertEquals(HttpStatusCode.OK, registerResponse.status)
        assertTrue(registerResponse.bodyAsText().contains("User Test"))

        // Login success
        val loginResponse = client.post("/login") {
            contentType(ContentType.Application.Json)
            setBody("""{"email":"test@example.com","password":"secret"}""")
        }
        assertEquals(HttpStatusCode.OK, loginResponse.status)
        assertTrue(loginResponse.bodyAsText().contains("Login successful"))

        // Login fail
        val loginFail = client.post("/login") {
            contentType(ContentType.Application.Json)
            setBody("""{"email":"test@example.com","password":"wrong"}""")
        }
        assertEquals(HttpStatusCode.OK, loginFail.status)
        assertTrue(loginFail.bodyAsText().contains("Invalid credentials"))
    }
}