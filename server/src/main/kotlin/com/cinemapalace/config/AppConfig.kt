package com.cinemapalace.config

import io.ktor.server.config.*

data class DatabaseConfig(val url: String)
data class JwtConfig(val secret: String, val issuer: String, val audience: String, val realm: String)
data class TmdbConfig(val apiKey: String, val baseUrl: String = "https://api.themoviedb.org/3")

data class AppConfig(
    val database: DatabaseConfig,
    val jwt: JwtConfig,
    val tmdb: TmdbConfig
) {
    companion object {
        fun fromApplicationConfig(config: ApplicationConfig): AppConfig {
            return AppConfig(
                database = DatabaseConfig(
                    url = config.propertyOrNull("ktor.database.url")?.getString()
                        ?: EnvConfig.get("DATABASE_URL", "jdbc:sqlite:./cinemapalace.db")
                ),
                jwt = JwtConfig(
                    secret = config.propertyOrNull("ktor.jwt.secret")?.getString()
                        ?: EnvConfig.get("JWT_SECRET", "dev-fallback-secret"),
                    issuer = config.propertyOrNull("ktor.jwt.issuer")?.getString() ?: "cinemapalace",
                    audience = config.propertyOrNull("ktor.jwt.audience")?.getString() ?: "users",
                    realm = config.propertyOrNull("ktor.jwt.realm")?.getString() ?: "cinemapalace app"
                ),
                tmdb = TmdbConfig(
                    apiKey = config.propertyOrNull("ktor.tmdb.apiKey")?.getString()
                        ?: EnvConfig.get("TMDB_API_KEY"),
                    baseUrl = config.propertyOrNull("ktor.tmdb.baseUrl")?.getString()
                        ?: "https://api.themoviedb.org/3"
                )
            )
        }
    }
}