pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        // 🧩 Nytt repo för Compose Compiler
        maven("https://androidx.dev/storage/compose-compiler/repository/")
    }

    plugins {
        id("com.android.application") version "8.7.1"
        id("org.jetbrains.kotlin.android") version "1.9.24"
        id("org.jetbrains.kotlin.jvm") version "1.9.24"
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        // 🧩 Lägg även här
        maven("https://androidx.dev/storage/compose-compiler/repository/")
    }
}

rootProject.name = "CinemaPalace"
include(":app")
include(":server")