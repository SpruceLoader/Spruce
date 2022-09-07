allprojects {
    repositories {
        // Default repositories
        mavenCentral()

        // Repositories
        maven("https://maven.unifycraft.xyz/releases")
        maven("https://libraries.minecraft.net/")
        maven("https://repo.spongepowered.org/maven/")
        maven("https://jitpack.io/")

        // Snapshots
        maven("https://maven.unifycraft.xyz/snapshots")
        mavenLocal()
    }
}
