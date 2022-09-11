package xyz.unifycraft.uniloader.loader.impl

import xyz.unifycraft.launchwrapper.api.ArgumentMap
import xyz.unifycraft.uniloader.api.Entrypoint
import xyz.unifycraft.uniloader.api.PreLaunchEntrypoint
import xyz.unifycraft.uniloader.loader.api.Environment
import xyz.unifycraft.uniloader.loader.api.UniLoader
import xyz.unifycraft.uniloader.loader.impl.discoverer.ModDiscoverer
import xyz.unifycraft.uniloader.loader.impl.discoverer.finders.ClasspathModFinder
import xyz.unifycraft.uniloader.loader.impl.discoverer.finders.DirectoryModFinder
import xyz.unifycraft.uniloader.loader.impl.entrypoints.EntrypointHandler
import xyz.unifycraft.uniloader.loader.impl.metadata.ModMetadata
import java.io.File
import java.lang.IllegalArgumentException

class UniLoaderImpl : UniLoader {
    private lateinit var currentEnvironment: Environment
    private lateinit var gameVersion: String
    private val discoverer = ModDiscoverer()
    private var loadingComplete = false

    override fun getEnvironment() = currentEnvironment
    override fun setEnvironment(environment: Environment) {
        this.currentEnvironment = environment
    }
    override fun getGameVersion() = gameVersion

    override fun getGameDir() = File(".")
    override fun getLoaderDir() = File(getGameDir(), "UniLoader")
    override fun getConfigDir() = File(getLoaderDir(), "config")
    override fun getDataDir() = File(getLoaderDir(), "data")
    override fun getModsDir() = File(getLoaderDir(), "mods")

    override fun isLoadingComplete() = loadingComplete
    override fun load(argMap: ArgumentMap) {
        gameVersion = argMap.getSingular("version")

        discoverer.addFinder(ClasspathModFinder())
        discoverer.addFinder(DirectoryModFinder(getModsDir()))
        discoverer.discover()

        for (entrypoint in getEntrypoints<PreLaunchEntrypoint>("preLaunch")) {
            entrypoint.initialize(argMap)
        }

        for (mod in discoverer.getMods()) {
            println("Mod ${mod.name} with ID ${mod.id} and version ${mod.version} was loaded successfully!")
        }

        loadingComplete = true
    }

    override fun getMod(id: String) = discoverer.getMods().firstOrNull {
        it.id.equals(id, true)
    } ?: throw IllegalArgumentException("Mod with ID \"$id\" is not present!")

    override fun getAllMods() = discoverer.getMods()

    override fun <T : Entrypoint> getEntrypoints(namespace: String): List<T> =
        EntrypointHandler.getEntrypoints(namespace)?.map { it as T } ?: emptyList()

    override fun isModLoaded(id: String) = discoverer.getMods().any {
        it.id.equals(id, true)
    }
}
