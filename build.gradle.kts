import org.quiltmc.gradle.licenser.extension.QuiltLicenserGradleExtension

plugins {
    id("org.quiltmc.gradle.licenser") version "2.+" apply false
}

subprojects {
    group = extra["project.group"]?.toString() ?: throw groovy.lang.MissingPropertyException("Project group was not set!")
    version = extra["project.version"]?.toString() ?: throw groovy.lang.MissingPropertyException("Project version was not set!")

    arrayOf(
        "java-library",
        "org.quiltmc.gradle.licenser",
    ).forEach {
        apply(plugin = it)
    }

    repositories {
        // Snapshots
        mavenLocal()

        // Default repositories
        mavenCentral()

        // Repositories
        maven("https://libraries.minecraft.net/")
        maven("https://repo.spongepowered.org/maven/")
        maven("https://jitpack.io/")
    }

    dependencies {
        "implementation"("org.jetbrains", "annotations", "24.0.1")
        "implementation"("org.apache.logging.log4j", "log4j-api", "2.14.1")
    }

    configure<QuiltLicenserGradleExtension> {
        rule(rootProject.projectDir.resolve("codeformat").resolve("HEADER"))
    }

    configure<JavaPluginExtension> {
        withJavadocJar()
        withSourcesJar()

        toolchain {
            languageVersion.set(JavaLanguageVersion.of(8))
        }
    }
}
