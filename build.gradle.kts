allprojects {
    repositories {
        // Default repositories
        mavenCentral()

        // Repositories
        maven("https://maven.unifycraft.xyz/releases")
        maven("https://jitpack.io/")

        // Snapshots
        maven("https://maven.unifycraft.xyz/snapshots")
        mavenLocal()
    }
}
