package xyz.unifycraft.uniloader.loader.impl.discoverer

import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import org.apache.logging.log4j.LogManager
import xyz.unifycraft.uniloader.loader.api.Environment
import xyz.unifycraft.uniloader.loader.api.UniLoader
import xyz.unifycraft.uniloader.loader.impl.discoverer.finders.ModFinder
import xyz.unifycraft.uniloader.loader.impl.metadata.*
import xyz.unifycraft.uniloader.loader.impl.metadata.parser.ModMetadataParser

class ModDiscoverer {
    companion object {
        private val logger = LogManager.getLogger("Mod Discoverer")
    }

    private val finders = mutableListOf<ModFinder>()
    private val mods = mutableListOf<ModMetadata>()

    fun discover() {
        val startTime = System.currentTimeMillis()
        loadBuiltInMods()

        val rawMetadata = mutableListOf<String>()
        for (finder in finders) {
            for (found in finder.find()) {
                rawMetadata.add(found)
            }
        }

        val metadata = rawMetadata.map(ModMetadataParser::read)

        metadata.forEach { println("meta: $it") }

        this.mods.addAll(metadata)
        logger.info("Discovered ${mods.size} mods in ${System.currentTimeMillis() - startTime}ms.")
    }

    fun addFinder(finder: ModFinder) {
        finders.add(finder)
    }

    private fun loadBuiltInMods() {
        mods.add(ModMetadata(
            schemaVersion = ModMetadata.CURRENT_SCHEMA_VERSION,

            name = "Minecraft",
            version = UniLoader.getInstance().getGameVersion(),
            id = "minecraft",
            type = ModType.LIBRARY,

            license = License("Unknown", null),

            contributors = listOf(Contributor("Mojang", "Author")),
            links = ModLinks(
                "https://minecraft.net",
                null,
                null,
                "https://discord.gg/minecraft"
            ),

            loader = LoaderData(
                environment = Environment.BOTH,
                accessWideners = emptyList(),
                mixins = emptyList(),
                entrypoints = emptyMap()
            ),

            additional = JsonObject()
        ))

        mods.add(
            ModMetadata(
            schemaVersion = ModMetadata.CURRENT_SCHEMA_VERSION,

                name = System.getProperty("java.vm.name"),
                version = System.getProperty("java.specification.version").replaceFirst("^1\\.", ""),
                id = "java",
                type = ModType.LIBRARY,

                license = License("Unknown", null),

                contributors = emptyList(),
                links = ModLinks(
                    "https://java.com",
                    null,
                    null,
                    null
                ),

                loader = LoaderData(
                    environment = Environment.BOTH,
                    accessWideners = emptyList(),
                    mixins = emptyList(),
                    entrypoints = emptyMap()
                ),

                additional = JsonObject()
            ))
    }

    fun getMods() = mods.toList()
}
