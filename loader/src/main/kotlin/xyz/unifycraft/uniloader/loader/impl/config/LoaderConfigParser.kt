package xyz.unifycraft.uniloader.loader.impl.config

import com.google.gson.stream.JsonReader
import xyz.unifycraft.uniloader.loader.api.UniLoader
import xyz.unifycraft.uniloader.loader.impl.utils.*
import java.io.File

object LoaderConfigParser : Parser<LoaderConfig> {
    override fun parse(input: String): LoaderConfig {
        var loadingScreen = false
        val modDirs = mutableListOf<File>()

        input.readJson { token ->
            requireObject("Loader config")

            forEachItemObject { token, name ->
                when (name) {
                    "loading_screen" -> loadingScreen = readBool("loading_screen")
                    "mod_dirs" -> modDirs.addAll(readModDirs())
                    else -> return@forEachItemObject true
                }
                false
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
        forEachItemArray {
            value.add(File(
                readString()
                    .replace("{GAME}", instance.gameDir.absolutePath)
                    .replace("{LOADER}", instance.loaderDir.absolutePath)
            ))
            false
        }

        endArray()
        return value
    }
}
