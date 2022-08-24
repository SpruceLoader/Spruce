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
        kotlin("plugin.serialization") version(kotlin)

        val ucgt = "1.11.0"
        id("xyz.unifycraft.gradle.multiversion-root") version(ucgt)
    }
}

val projectName: String = extra["mod.name"]?.toString()
    ?: throw MissingPropertyException("mod.name has not been set.")
rootProject.name = projectName
rootProject.buildFileName = "build.gradle.kts"

listOf(
    "1.18.2-fabric",
    "1.19.2-fabric"
).forEach { version ->
    include(":$version")
    project(":$version").apply {
        projectDir = file("versions/$version")
        buildFileName = "../../version.gradle.kts"
    }
}