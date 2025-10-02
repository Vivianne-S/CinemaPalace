package com.cinemapalace.config

import io.github.cdimascio.dotenv.Dotenv

object EnvConfig {
    private val dotenv: Dotenv = Dotenv.configure()
        .ignoreIfMissing() // kör även om .env inte finns
        .load()

    fun get(key: String, default: String? = null): String {
        return dotenv[key] ?: System.getenv(key) ?: default
        ?: throw IllegalArgumentException("Missing required environment variable: $key")
    }
}