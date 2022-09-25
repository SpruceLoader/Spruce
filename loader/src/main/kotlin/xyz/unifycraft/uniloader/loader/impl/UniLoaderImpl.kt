package xyz.unifycraft.uniloader.loader.impl

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.stream.JsonReader
import org.apache.logging.log4j.LogManager
import xyz.unifycraft.launchwrapper.api.ArgumentMap
import xyz.unifycraft.uniloader.api.Entrypoint
import xyz.unifycraft.uniloader.api.PreLaunchEntrypoint
import xyz.unifycraft.uniloader.loader.api.Environment
import xyz.unifycraft.uniloader.loader.api.UniLoader
import xyz.unifycraft.uniloader.loader.exceptions.MissingDependencyException
import xyz.unifycraft.uniloader.loader.impl.discoverer.ModDiscoverer
import xyz.unifycraft.uniloader.loader.impl.discoverer.finders.ClasspathModFinder
import xyz.unifycraft.uniloader.loader.impl.discoverer.finders.DirectoryModFinder
import xyz.unifycraft.uniloader.loader.impl.entrypoints.EntrypointHandler
import xyz.unifycraft.uniloader.loader.impl.metadata.ModMetadata
import java.io.File
import java.io.FileReader
import java.io.InputStreamReader
import java.io.StringReader
import java.lang.IllegalArgumentException
import java.nio.file.Files
import java.nio.file.Paths

class UniLoaderImpl : UniLoader {
    companion object {
        private val logger = LogManager.getLogger(UniLoader.NAME)
    }

    private lateinit var currentEnvironment: Environment
    override lateinit var gameVersion: String
    private val discoverer = ModDiscoverer()

    override var environment: Environment
        get() = currentEnvironment
        set(value) {
            currentEnvironment = value
        }

    override val gameDir = File(".")
    override val loaderDir = File(gameDir, "UniLoader")
    override val configDir = File(loaderDir, "config")
    override val dataDir = File(loaderDir, "data")
    override val modsDir = File(loaderDir, "mods")

    override var isLoadingComplete = false
    override fun load(argMap: ArgumentMap) {
        println("Printing args...")
        argMap.toArray().forEach(System.out::println)

        if (currentEnvironment == Environment.BOTH) {
            logger.error("How... How did you manage to do this..? (current environment is \"BOTH\")")
            throw IllegalStateException("Weird environment... $currentEnvironment")
        }

        //FIXME: Make this look better (trust me its way better then before)
        gameVersion = Gson().fromJson(InputStreamReader(javaClass.classLoader.getResourceAsStream("version.json")!!),MinecraftVersion::class.java).id

        discoverer.addFinder(ClasspathModFinder())
        discoverer.addFinder(DirectoryModFinder(modsDir))
        discoverer.discover()

        val mods = allMods
        for (mod in mods) {
            println("Mod ${mod.name} with ID ${mod.id} and version ${mod.version} was loaded successfully!")
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
