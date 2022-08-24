import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import com.modrinth.minotaur.dependencies.ModDependency
import com.modrinth.minotaur.dependencies.DependencyType
import xyz.unifycraft.gradle.utils.GameSide
import xyz.unifycraft.gradle.tools.CurseDependency

plugins {
    java
    kotlin("jvm")
    kotlin("plugin.serialization")
    id("xyz.unifycraft.gradle.multiversion")
    id("xyz.unifycraft.gradle.tools")
    id("xyz.unifycraft.gradle.tools.loom")
}

loomHelper {
    disableRunConfigs(GameSide.SERVER)
}

repositories {
    maven("https://maven.terraformersmc.com/")
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))

    modImplementation("net.fabricmc.fabric-api:fabric-api:${when (mcData.version) {
        11902 -> "0.57.0+1.19"
        11802 -> "0.57.0+1.18.2"
        else -> throw IllegalStateException("Invalid MC version: ${mcData.version}")
    }}")
    modImplementation("net.fabricmc:fabric-language-kotlin:1.8.2+kotlin.1.7.10")

    modImplementation("com.terraformersmc:modmenu:${when (mcData.version) {
        11902 -> "4.0.4"
        11802 -> "3.2.3"
        else -> throw IllegalStateException("Invalid MC version: ${mcData.version}")
    }}")
}

tasks {
    withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs += "-Xjvm-default=enable"
        }
    }

    remapJar {
        archiveBaseName.set("${modData.name}-${mcData.versionStr}")
    }
}
