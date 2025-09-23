plugins {
    id("org.jetbrains.kotlin.jvm")
    application
}

dependencies {
    implementation("io.ktor:ktor-server-core:2.3.9")
    implementation("io.ktor:ktor-server-netty:2.3.9")
    implementation("io.ktor:ktor-server-content-negotiation:2.3.9")
    implementation("io.ktor:ktor-serialization-jackson:2.3.9")

    implementation("ch.qos.logback:logback-classic:1.4.14")
}
application {
    mainClass.set("com.cinemapalace.ApplicationKt")
}