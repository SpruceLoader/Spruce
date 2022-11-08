package xyz.spruceloader.loader.impl

import org.slf4j.LoggerFactory
import xyz.spruceloader.launchwrapper.Launch
import xyz.spruceloader.launchwrapper.api.ArgumentMap
import xyz.spruceloader.launchwrapper.api.EnvSide
import xyz.spruceloader.api.Entrypoint
import xyz.spruceloader.api.PreLaunchEntrypoint
import xyz.spruceloader.loader.api.Environment
import xyz.spruceloader.loader.api.SpruceLoader
import xyz.spruceloader.loader.exceptions.InvalidMetadataException
import xyz.spruceloader.loader.exceptions.MissingDependencyException
import xyz.spruceloader.loader.impl.config.LoaderConfig
import xyz.spruceloader.loader.impl.config.LoaderConfigParser
import xyz.spruceloader.loader.impl.discoverer.ModDiscoverer
import xyz.spruceloader.loader.impl.discoverer.finders.ClasspathModFinder
import xyz.spruceloader.loader.impl.discoverer.finders.DirectoryModFinder
import xyz.spruceloader.loader.impl.entrypoints.EntrypointHandler
import xyz.spruceloader.loader.impl.game.MinecraftVersion
import xyz.spruceloader.loader.impl.game.MinecraftVersionParser
import xyz.spruceloader.loader.impl.metadata.ModMetadata
import java.io.File
import java.lang.IllegalArgumentException

class SpruceLoaderImpl : SpruceLoader {
    companion object {
        private val logger = LoggerFactory.getLogger(SpruceLoader.NAME)
    }

    override lateinit var environment: Environment
    override lateinit var gameVersion: MinecraftVersion

    private val discoverer = ModDiscoverer()

    override lateinit var gameDir: File
    override val loaderDir = File(gameDir, SpruceLoader.NAME)
    override val configDir = File(loaderDir, "config")
    override val dataDir = File(loaderDir, "data")
    override val modsDir = File(loaderDir, "mods")

    override lateinit var loaderConfig: LoaderConfig
    override var isLoadingComplete = false

    override fun load(argMap: ArgumentMap, env: EnvSide) {
        environment = Environment.valueOf(env.name)
        if (environment == Environment.BOTH) {
            logger.error("How... How did you manage to do this..? (current environment is \"BOTH\")")
            throw IllegalStateException("Weird environment... $environment")
        }

        gameDir = File(argMap.getSingular("gameDir") ?: throw IllegalArgumentException("gameDir is not present!"))
        if (!loaderDir.exists() && !loaderDir.mkdirs())
            throw IllegalStateException("Could not create ${SpruceLoader.NAME} directory!")
        if (!configDir.exists() && !configDir.mkdirs())
            throw IllegalStateException("Could not create config directory!")
        if (!dataDir.exists() && !dataDir.mkdirs())
            throw IllegalStateException("Could not create data directory!")

        loaderConfig = LoaderConfigParser.parse(File(loaderDir, "config.json").apply {
            if (!exists()) {
                createNewFile()
                writeText(LoaderConfig.serialize(LoaderConfig.DEFAULT))
            }
        }.readText())
        gameVersion = MinecraftVersionParser.parse(Launch.getInstance().classLoader.getResourceAsStream("version.json")?.readBytes()?.decodeToString() ?: throw InvalidMetadataException("Couldn't determine Minecraft version?"))

        discoverer.addFinder(ClasspathModFinder())
        discoverer.addFinder(DirectoryModFinder(modsDir))
        loaderConfig.modDirs.forEach { discoverer.addFinder(DirectoryModFinder(it)) }
        discoverer.discover()

        val mods = allMods
        for (mod in mods) {
            println("Mod ${mod.name} with ID ${mod.id} and version ${mod.version.readableString} was loaded successfully!")
            val dependencies = mod.loader?.dependencies ?: continue
            dependencies.forEach { dependency ->
                if (mods.any {
                    dependency.unless.contains(it.id)
                }) return@forEach
                if (dependency.only.isNotEmpty() && mods.none {
                    dependency.only.contains(it.id)
                }) return@forEach
                if (mods.none {
                    it.id.equals(dependency.id, true) && it.version <= dependency.version
                }) throw MissingDependencyException("${mod.name} requires a mod with the ID ${dependency.id}, but that mod isn't present!")
            }
            println("${mod.name} dependencies: $dependencies")
        }

        for (entrypoint in getEntrypoints<PreLaunchEntrypoint>("preLaunch")) {
            entrypoint.initialize(argMap)
        }

        isLoadingComplete = true
    }

    override fun getMod(id: String) = discoverer.getMods().firstOrNull {
        it.id.equals(id, true)
    } ?: throw IllegalArgumentException("Mod with ID \"$id\" is not present!")

    override val allMods: List<ModMetadata>
        get() = discoverer.getMods()

    override fun <T : Entrypoint> getEntrypoints(namespace: String): List<T> =
        EntrypointHandler.getEntrypoints(namespace)?.map { it as T } ?: emptyList()

    override fun isModLoaded(id: String) = discoverer.getMods().any {
        it.id.equals(id, true)
    }
}
