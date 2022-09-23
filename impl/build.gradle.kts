plugins {
    java
    kotlin("jvm")
    id("xyz.unifycraft.uniloom") version("1.0.0-beta.20")
}

val internal by configurations.creating {
    configurations.implementation.get().extendsFrom(this)
    configurations.modCompileClasspath.get().extendsFrom(this)
}

repositories {
    mavenCentral()
}

dependencies {
    minecraft("com.mojang:minecraft:1.19.2")
    mappings("net.fabricmc:yarn:1.19.2+build.8")

    internal(project(":loader"))
}
