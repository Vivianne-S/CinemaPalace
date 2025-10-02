plugins {
    kotlin("jvm") // ✅ ingen version här – version kommer från pluginManagement/libs.versions.toml
    application
}

dependencies {
    // 🔹 Ktor server
    implementation("io.ktor:ktor-server-core:2.3.9")
    implementation("io.ktor:ktor-server-netty:2.3.9")
    implementation("io.ktor:ktor-server-content-negotiation:2.3.9")
    implementation("io.ktor:ktor-serialization-jackson:2.3.9")

    // 🔹 Logging
    implementation("ch.qos.logback:logback-classic:1.4.14")

    // 🔹 Dotenv loader
    implementation("io.github.cdimascio:dotenv-kotlin:6.4.1")

    // 🔹 Exposed ORM
    implementation("org.jetbrains.exposed:exposed-core:0.44.0")
    implementation("org.jetbrains.exposed:exposed-dao:0.44.0")
    implementation("org.jetbrains.exposed:exposed-jdbc:0.44.0")
    implementation("org.jetbrains.exposed:exposed-java-time:0.44.0")

    // 🔹 SQLite JDBC driver
    implementation("org.xerial:sqlite-jdbc:3.45.1.0")

    // 🔹 BCrypt
    implementation("org.mindrot:jbcrypt:0.4")

    // 🔹 Ktor Client (exempel: TMDB)
    implementation("io.ktor:ktor-client-core:2.3.9")
    implementation("io.ktor:ktor-client-cio:2.3.9")
    implementation("io.ktor:ktor-client-content-negotiation:2.3.9")

    // 🔹 Ktor Auth + JWT
    implementation("io.ktor:ktor-server-auth:2.3.9")
    implementation("io.ktor:ktor-server-auth-jwt:2.3.9")
    implementation("com.auth0:java-jwt:4.4.0")

    // 🔹 Test
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.0")
}

application {
    mainClass.set("com.cinemapalace.ApplicationKt")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

tasks.test {
    useJUnitPlatform()
}