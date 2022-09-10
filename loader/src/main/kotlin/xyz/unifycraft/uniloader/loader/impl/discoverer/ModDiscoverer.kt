package xyz.unifycraft.uniloader.loader.impl.discoverer

import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import org.apache.logging.log4j.LogManager
import xyz.unifycraft.launchwrapper.Launch
import xyz.unifycraft.uniloader.loader.api.Environment
import xyz.unifycraft.uniloader.loader.api.UniLoader
import xyz.unifycraft.uniloader.loader.exceptions.ModDiscoveryException
import xyz.unifycraft.uniloader.loader.impl.discoverer.finders.ModFinder
import xyz.unifycraft.uniloader.loader.impl.metadata.*
import xyz.unifycraft.uniloader.loader.impl.metadata.parser.ModMetadataParser
import java.io.ByteArrayOutputStream
import java.io.File
import java.net.JarURLConnection
import java.nio.charset.StandardCharsets
import java.util.jar.JarFile

class ModDiscoverer {
    companion object {
        private val logger = LogManager.getLogger("Mod Discoverer")
    }

    private val finders = mutableListOf<ModFinder>()
    private val mods = mutableListOf<ModMetadata>()

    fun discover() {
        val startTime = System.currentTimeMillis()
        loadBuiltInMods()

        val modFiles = mutableListOf<File>()
        for (finder in finders) {
            for (found in finder.find()) {
                modFiles.add(found)
            }
        }

        val rawMetadata = mutableListOf<String>()
        for (file in modFiles) {
            if (file.isFile) { // Assuming this is a mod JAR file
                if (!file.extension.endsWith("jar")) throw ModDiscoveryException("Invalid mod file found!")

                val jar = JarFile(file)
                val entry = jar.getInputStream(jar.getEntry(ModMetadata.FILE_NAME))
                val outputStream = ByteArrayOutputStream()
                entry.copyTo(outputStream)
                rawMetadata.add(String(outputStream.toByteArray(), StandardCharsets.UTF_8))

                Launch.getInstance().addToClassPath(file.toPath())
            } else if (file.isDirectory) { // Assuming we're in the current classpath
                rawMetadata.add(File(file, ModMetadata.FILE_NAME).readText())
            } else continue
        }

        val metadata = rawMetadata.map(ModMetadataParser::read)

        this.mods.addAll(metadata)
        logger.info("Discovered ${mods.size} mods in ${System.currentTimeMillis() - startTime}ms.")
    }

    fun addFinder(finder: ModFinder) {
        finders.add(finder)
    }

    private fun loadBuiltInMods() {
        mods.add(builtinMod {
            name = "Minecraft"
            version = UniLoader.getInstance().getGameVersion()
            id = "minecraft"

            license {
                name = "Unknown"
            }

            contributor {
                name = "Mojang Studios"
                role = "Author"
            }

            links {
                home = "https://minecraft.net"
                discord = "https://discord.gg/minecract"
            }
        })

        mods.add(builtinMod {
            name = System.getProperty("java.vm.name")
            version = System.getProperty("java.specification.version").replaceFirst("^1\\.", "")
            id = "java"

            links {
                home = "https://java.com"
            }
        })
    }

    fun getMods() = mods.toList()
}
