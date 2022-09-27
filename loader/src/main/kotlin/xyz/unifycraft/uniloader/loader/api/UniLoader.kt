package xyz.unifycraft.uniloader.loader.api

import xyz.unifycraft.launchwrapper.api.ArgumentMap
import xyz.unifycraft.uniloader.api.Entrypoint
import xyz.unifycraft.uniloader.loader.impl.UniLoaderImpl
import xyz.unifycraft.uniloader.loader.impl.config.LoaderConfig
import xyz.unifycraft.uniloader.loader.impl.game.MinecraftVersion
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
     * @param <set-?> The current environment state.
     */
    var environment: Environment
    /**
     * @return The current Minecraft version which is being played on.
     */
    val gameVersion: MinecraftVersion

    /**
     * @return The base directory that the game launches in.
     */
    val gameDir: File
    /**
     * @return The directory used for UniLoader specifically.
     * This is NOT the base game directory, but instead a "UniLoader" subdirectory.
     */
    val loaderDir: File
    /**
     * @return The directory specifically made for the purpose of storing
     * mod configuration files.
     */
    val configDir: File
    /**
     * @return The directory made for storing mod's data, whether it be temporary
     * or not.
     */
    val dataDir: File
    /**
     * @return The directory which stores all the individual mod files.
     */
    val modsDir: File

    /**
     * @return Whether the mod loading process is finished or not, mostly
     * used in internals.
     */
    val isLoadingComplete: Boolean
    /**
     * @return The config for the loader itself.
     */
    val loaderConfig: LoaderConfig
    /**
     * Loads all mods in the classpath, mods directory and command-line
     * into the game using our custom class loader.
     */
    fun load(argMap: ArgumentMap)

    fun getMod(id: String): ModMetadata
    val allMods: List<ModMetadata>
    fun <T : Entrypoint> getEntrypoints(namespace: String): List<T>
    fun isModLoaded(id: String): Boolean
}
