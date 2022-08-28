package xyz.unifycraft.uniloader.loader.api

import xyz.unifycraft.uniloader.loader.impl.UniLoaderImpl
import java.io.File

interface UniLoader {
    companion object {
        private lateinit var instance: UniLoader

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
     * @return Whether we're currently in a development environment or not.
     */
    fun isDev(): Boolean

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

    // TODO - fun getMod(id: String): ModMetadata
    // TODO - fun getAllMods(): List<ModMetadata>
    // TODO - fun <T> getEntrypoints(namespace: String, type: Class<T>): List<T>
    // TODO - fun isModLoaded(id: String): Boolean
}
