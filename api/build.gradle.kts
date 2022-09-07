import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    java
    kotlin("jvm")
    // id("xyz.unifycraft.uniloom") version("1.0.0-beta.6")
}

group = extra["project.group"]?.toString()
    ?: throw groovy.lang.MissingPropertyException("Project group was not set!")
version = extra["project.version"]?.toString()
    ?: throw groovy.lang.MissingPropertyException("Project version was not set!")

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    //implementation(project(":loader"))
    implementation("xyz.unifycraft:UEventBus:1.0.0")
}

tasks {
    withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs += "-Xjvm-default=enable"
        }
    }
}
