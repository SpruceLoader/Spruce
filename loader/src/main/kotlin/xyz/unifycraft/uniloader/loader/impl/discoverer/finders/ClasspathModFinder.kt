package xyz.unifycraft.uniloader.loader.impl.discoverer.finders

import org.apache.logging.log4j.LogManager
import xyz.unifycraft.launchwrapper.Launch
import xyz.unifycraft.uniloader.loader.impl.metadata.ModMetadata
import java.net.URL
import java.nio.file.Path
import java.nio.file.Paths

class ClasspathModFinder : ModFinder {
    companion object {
        private val logger = LogManager.getLogger("Classpath Mod Finder")
    }

    override fun find(): List<String> {
        if (!Launch.getInstance().isDevelopment)
            return emptyList()

        val returnValue = mutableListOf<String>()
        val modEnumeration = Launch::class.java.classLoader.getResources(ModMetadata.FILE_NAME)

        while (modEnumeration.hasMoreElements()) {
            val url = modEnumeration.nextElement()
            try {
                val path = fetchCodeSource(url, ModMetadata.FILE_NAME)
                val file = path.toFile()
                returnValue.add(file.readText())
            } catch (e: Exception) {
                logger.error("Failed to find location of ${ModMetadata.FILE_NAME} file from $url", e)
            }
        }

        return returnValue
    }

    private fun fetchCodeSource(url: URL, path: String): Path {
        val urlPath = url.path
        if (urlPath.endsWith(path))
            return Paths.get(URL(url.protocol, url.host, url.port, urlPath).toURI())

        throw IllegalStateException("Failed to fetch code source for file \"$path\" inside \"$url\"!")
    }
}
