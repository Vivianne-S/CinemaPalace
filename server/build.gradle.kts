plugins {
    id("org.jetbrains.kotlin.jvm")
    application
}

dependencies {
    // 🔹 Ktor server
    implementation("io.ktor:ktor-server-core-jvm:2.3.9")
    implementation("io.ktor:ktor-server-netty-jvm:2.3.9")
    implementation("io.ktor:ktor-server-content-negotiation-jvm:2.3.9")
    implementation("io.ktor:ktor-serialization-jackson-jvm:2.3.9")

    // 🔹 Logging
    implementation("ch.qos.logback:logback-classic:1.4.14")

    // 🔹 Exposed ORM (Kotlin SQL framework)
    implementation("org.jetbrains.exposed:exposed-core:0.54.0")
    implementation("org.jetbrains.exposed:exposed-dao:0.54.0")
    implementation("org.jetbrains.exposed:exposed-jdbc:0.54.0")
    implementation("org.jetbrains.exposed:exposed-java-time:0.54.0")

    // 🔹 SQLite JDBC driver
    implementation("org.xerial:sqlite-jdbc:3.45.1.0")

    // 🔹 BCrypt
    implementation("org.mindrot:jbcrypt:0.4")

    // ✅ Ktor test framework
    testImplementation("io.ktor:ktor-server-tests-jvm:2.3.9")
    testImplementation("org.jetbrains.kotlin:kotlin-test")

    // ✅ JUnit 5
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