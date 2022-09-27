package xyz.unifycraft.uniloader.loader.impl.config

import com.google.gson.stream.JsonReader
import org.apache.logging.log4j.LogManager
import xyz.unifycraft.uniloader.loader.api.UniLoader
import xyz.unifycraft.uniloader.loader.impl.utils.*
import java.io.File

object LoaderConfigParser : Parser<LoaderConfig> {
    private val logger = LogManager.getLogger("Loader Config Parser")

    override fun parse(input: String): LoaderConfig {
        var loadingScreen = false
        val modDirs = mutableListOf<File>()

        input.readJson { token ->
            requireObject("Loader config")

            forEachItemObject { token, name ->
                when (name) {
                    "loading_screen" -> loadingScreen = readBool("loading_screen")
                    "mod_dirs" -> modDirs.addAll(readModDirs())
                    else -> {
                        logger.warn("Unknown value found - skipping. ($name)")
                        skipValue()
                    }
                }
            }

            endObject()
        }

        return LoaderConfig(
            loadingScreen, modDirs
        )
    }

    private fun JsonReader.readModDirs(): List<File> {
        requireArray("mod_dirs")
        val value = mutableListOf<File>()

        val instance = UniLoader.getInstance()
        forEachItemArray { token ->
            value.add(File(
                readString()
                    .replace("{GAME}", instance.gameDir.absolutePath)
                    .replace("{LOADER}", instance.loaderDir.absolutePath)
            ))
        }

        endArray()
        return value
    }
}
