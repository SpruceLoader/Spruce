package xyz.unifycraft.uniloader.loader.api

import xyz.unifycraft.launchwrapper.api.ArgumentMap
import xyz.unifycraft.uniloader.api.Entrypoint
import xyz.unifycraft.uniloader.loader.impl.UniLoaderImpl
import xyz.unifycraft.uniloader.loader.impl.metadata.ModMetadata
import java.io.File

interface UniLoader {
    companion object {
        private lateinit var instance: UniLoader

        const val NAME = "@NAME@"
        const val VERSION = "@VERSION@"

        @JvmStatic
        fun getInstance(): UniLoader {
            if (!::instance.isInitialized) instance = UniLoaderImpl()
            return instance
        }
    }

    /**
     * @return The current game environment we're in.
     */
    fun getEnvironment(): Environment
    /**
     * This method is for **INTERNAL USE ONLY**. If it's called
     * after the initial environment has been set, it will throw
     * an exception.
     *
     * @param environment The current environment state.
     */
    fun setEnvironment(environment: Environment)
    /**
     * @return The current Minecraft version which is being played on.
     */
    fun getGameVersion(): String

    /**
     * @return The base directory that the game launches in.
     */
    fun getGameDir(): File
    /**
     * @return The directory used for UniLoader specifically.
     * This is NOT the base game directory, but instead a "UniLoader" subdirectory.
     */
    fun getLoaderDir(): File
    /**
     * @return The directory specifically made for the purpose of storing
     * mod configuration files.
     */
    fun getConfigDir(): File
    /**
     * @return The directory made for storing mod's data, whether it be temporary
     * or not.
     */
    fun getDataDir(): File
    /**
     * @return The directory which stores all the individual mod files.
     */
    fun getModsDir(): File

    /**
     * Loads all mods in the classpath, mods directory and command-line
     * into the game using our custom class loader.
     */
    fun load(argMap: ArgumentMap)

    fun getMod(id: String): ModMetadata
    fun getAllMods(): List<ModMetadata>
    fun <T : Entrypoint> getEntrypoints(namespace: String, type: Class<T>): List<T>
    fun isModLoaded(id: String): Boolean
}
