package xyz.unifycraft.uniloader.loader.impl

import xyz.unifycraft.launchwrapper.api.ArgumentMap
import xyz.unifycraft.uniloader.api.Entrypoint
import xyz.unifycraft.uniloader.api.PreLaunchEntrypoint
import xyz.unifycraft.uniloader.loader.api.Environment
import xyz.unifycraft.uniloader.loader.api.UniLoader
import xyz.unifycraft.uniloader.loader.impl.discoverer.ModDiscoverer
import xyz.unifycraft.uniloader.loader.impl.discoverer.finders.ClasspathModFinder
import xyz.unifycraft.uniloader.loader.impl.discoverer.finders.DirectoryModFinder
import xyz.unifycraft.uniloader.loader.impl.metadata.ModMetadata
import java.io.File

class UniLoaderImpl : UniLoader {
    private lateinit var currentEnvironment: Environment
    private lateinit var gameVersion: String
    private val discoverer = ModDiscoverer()

    override fun getEnvironment() = currentEnvironment
    override fun setEnvironment(environment: Environment) {
        this.currentEnvironment = environment
    }
    override fun getGameVersion() = gameVersion

    override fun getGameDir() = File(".")
    override fun getLoaderDir() = File(getGameDir(), "UniLoader")
    override fun getConfigDir() = File(getLoaderDir(), "config")
    override fun getDataDir() = File(getLoaderDir(), "data")
    override fun getModsDir() = File("mods")

    override fun load(argMap: ArgumentMap) {
        gameVersion = argMap.getSingular("version")

        discoverer.addFinder(ClasspathModFinder())
        discoverer.addFinder(DirectoryModFinder(getModsDir()))
        discoverer.discover()

        for (entrypoint in getEntrypoints("preLaunch", PreLaunchEntrypoint::class.java)) {
            entrypoint.initialize(argMap)
        }

        for (mod in discoverer.getMods()) {
            println("Mod ${mod.name} with ID ${mod.id} and version ${mod.version} was loaded successfully!")
        }
    }

    override fun getMod(id: String): ModMetadata {
        TODO("Not yet implemented")
    }

    override fun getAllMods(): List<ModMetadata> {
        TODO("Not yet implemented")
    }

    override fun <T : Entrypoint> getEntrypoints(namespace: String, type: Class<T>): List<T> {
        //TODO("Not yet implemented")
        return listOf()
    }

    override fun isModLoaded(id: String): Boolean {
        TODO("Not yet implemented")
    }
}
