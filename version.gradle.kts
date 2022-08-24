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
    implementation("xyz.unifycraft:UEventBus:1.0.0")
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
