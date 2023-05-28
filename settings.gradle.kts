import groovy.lang.MissingPropertyException

pluginManagement {
    repositories {
        // Snapshots
        mavenLocal()

        // Default repositories
        gradlePluginPortal()
        mavenCentral()

        // Repositories
        maven("https://maven.fabricmc.net")
        maven("https://maven.architectury.dev/")
        maven("https://maven.minecraftforge.net")
        maven("https://jitpack.io/")
    }
}

val projectName: String = extra["project.name"]?.toString()
    ?: throw MissingPropertyException("Project name was not set!")
rootProject.name = projectName

include("launch")
include("loader")
