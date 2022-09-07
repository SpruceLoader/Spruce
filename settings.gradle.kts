import groovy.lang.MissingPropertyException

pluginManagement {
    repositories {
        // Default repositories
        gradlePluginPortal()
        mavenCentral()

        // Repositories
        maven("https://maven.unifycraft.xyz/releases")
        maven("https://maven.fabricmc.net")
        maven("https://maven.architectury.dev/")
        maven("https://maven.minecraftforge.net")
        maven("https://repo.essential.gg/repository/maven-public")
        maven("https://server.bbkr.space/artifactory/libs-release/")
        maven("https://jitpack.io/")

        // Snapshots
        maven("https://maven.unifycraft.xyz/snapshots")
        maven("https://s01.oss.sonatype.org/content/groups/public/")
        mavenLocal()
    }

    plugins {
        val kotlin = "1.6.21"
        kotlin("jvm") version(kotlin)
    }
}

val projectName: String = extra["project.name"]?.toString()
    ?: throw MissingPropertyException("Project name was not set!")
rootProject.name = projectName

// Next, we need to load all available mods
include(":loader")
// We also provide an in-depth API alongside our loader
include(":api")
// Here we implement the classes in our API
include(":impl")
