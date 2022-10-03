plugins {
    java
    kotlin("jvm")
    `maven-publish`
}

repositories {
    mavenCentral()
    maven("https://libraries.minecraft.net/")
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("com.google.code.gson:gson:2.9.0")

    implementation("xyz.unifycraft:UniLaunchwrapper:1.0.2")
    implementation("org.ow2.asm:asm-tree:9.3")
    implementation("xyz.unifycraft:ULASM:1.0.0-beta.12")

    compileOnly("org.apache.logging.log4j:log4j-api:2.8.1")
    compileOnly("org.slf4j:slf4j-api:1.8.0-beta4")
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            artifactId = extra["project.name"]?.toString() ?: throw IllegalArgumentException("The project name has not been set.")
            groupId = project.group.toString()
            version = project.version.toString()

            from(components["java"])
        }
    }

    repositories {
        if (project.hasProperty("unifycraft.publishing.username") && project.hasProperty("unifycraft.publishing.password")) {
            fun MavenArtifactRepository.applyCredentials() {
                credentials {
                    username = property("unifycraft.publishing.username")?.toString()
                    password = property("unifycraft.publishing.password")?.toString()
                }
                authentication.create<BasicAuthentication>("basic")
            }

            maven {
                name = "UnifyCraftRelease"
                url = uri("https://maven.unifycraft.xyz/releases")
                applyCredentials()
            }

            maven {
                name = "UnifyCraftSnapshots"
                url = uri("https://maven.unifycraft.xyz/snapshots")
                applyCredentials()
            }
        }
    }
}
