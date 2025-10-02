package com.cinemapalace

import com.cinemapalace.api.movieRoutes
import com.cinemapalace.config.AppConfig
import com.cinemapalace.database.DatabaseFactory
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.SerializationFeature
import io.github.cdimascio.dotenv.dotenv
import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm

fun main() {
    // 🔹 Ladda .env
    val dotenv = dotenv {
        ignoreIfMissing = true
    }
    dotenv.entries().forEach { entry ->
        System.setProperty(entry.key, entry.value)
    }

    embeddedServer(Netty, port = 8080) {
        module()
    }.start(wait = true)
}

fun Application.module() {
    val appConfig = AppConfig.fromApplicationConfig(environment.config)

    // ✅ Init DB
    DatabaseFactory.init(appConfig.database)

    // ✅ HttpClient för TMDB
    val client = HttpClient {
        install(io.ktor.client.plugins.contentnegotiation.ContentNegotiation) {
            jackson {
                configure(SerializationFeature.INDENT_OUTPUT, true)
                configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            }
        }
    }

    // ✅ Serverns ContentNegotiation
    install(ContentNegotiation) {
        jackson {
            disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        }
    }

    // ✅ JWT (för bokningar & auth)
    install(Authentication) {
        jwt("auth-jwt") {
            realm = appConfig.jwt.realm
            verifier(
                JWT
                    .require(Algorithm.HMAC256(appConfig.jwt.secret))
                    .withAudience(appConfig.jwt.audience)
                    .withIssuer(appConfig.jwt.issuer)
                    .build()
            )
            validate { credential ->
                if (credential.payload.getClaim("userId").asString().isNotEmpty()) {
                    JWTPrincipal(credential.payload)
                } else null
            }
        }
    }

    // ✅ Routing
    routing {
        get("/health") {
            call.respond(mapOf("status" to "healthy"))
        }

        movieRoutes(appConfig.tmdb, client)
    }
}