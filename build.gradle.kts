plugins {
    id("xyz.unifycraft.uniloom") version("1.0.0-beta.21") apply(false)
}

allprojects {
    group = extra["project.group"]?.toString() ?: throw groovy.lang.MissingPropertyException("Project group was not set!")
    version = extra["project.version"]?.toString() ?: throw groovy.lang.MissingPropertyException("Project version was not set!")

    repositories {
        // Snapshots
        mavenLocal()

        // Default repositories
        mavenCentral()

        // Repositories
        maven("https://libraries.minecraft.net/")
        maven("https://repo.spongepowered.org/maven/")
        maven("https://jitpack.io/")
        maven("https://maven.spruceloader.xyz/releases")
        maven("https://maven.spruceloader.xyz/snapshots")
        maven("https://maven.enhancedpixel.xyz/releases")
        maven("https://maven.enhancedpixel.xyz/snapshots")
    }
}
