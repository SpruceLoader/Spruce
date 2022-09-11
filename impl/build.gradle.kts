plugins {
    java
    kotlin("jvm")
    id("xyz.unifycraft.uniloom") version("1.0.0-beta.18")
}

group = extra["project.group"]?.toString()
    ?: throw groovy.lang.MissingPropertyException("Project group was not set!")
version = extra["project.version"]?.toString()
    ?: throw groovy.lang.MissingPropertyException("Project version was not set!")

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

    implementation("xyz.unifycraft:UniLaunchwrapper:1.0.1")
    internal(project(":loader"))
}
