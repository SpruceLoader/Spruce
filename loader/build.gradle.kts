plugins {
    java
    kotlin("jvm")
}

repositories {
    mavenCentral()
    maven("https://libraries.minecraft.net/")
    maven("https://repo.spongepowered.org/maven/")
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("org.spongepowered:mixin:0.8.5-SNAPSHOT")
    implementation("net.minecraft:launchwrapper:1.12")
}
