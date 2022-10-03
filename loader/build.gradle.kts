plugins {
    `java-library`
    kotlin("jvm")
    `maven-publish`
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("com.google.code.gson:gson:2.9.0")

    implementation("xyz.unifycraft:UniLaunchwrapper:1.0.2")
    implementation("org.ow2.asm:asm-tree:9.3")
    implementation("xyz.unifycraft:ByteTransform:1.0.0-alpha.12")

    val slf4j = "2.0.0"
    implementation("org.slf4j:slf4j-api:$slf4j")
    implementation("org.slf4j:slf4j-ext:$slf4j")

    val log4j = "2.19.0"
    implementation("org.apache.logging.log4j:log4j-api:$log4j")
    implementation("org.apache.logging.log4j:log4j-core:$log4j")
    implementation("org.apache.logging.log4j:log4j-slf4j2-impl:$log4j")
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
