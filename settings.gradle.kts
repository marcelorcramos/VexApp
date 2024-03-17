pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven("https://kotlin.bintray.com/kotlinx")
    }
    plugins {
        id("com.google.devtools.ksp") version "1.9.23-1.0.19" apply false
        id("org.jetbrains.kotlin.android") version "1.9.0"
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "VexAplicação2"
include(":app")
