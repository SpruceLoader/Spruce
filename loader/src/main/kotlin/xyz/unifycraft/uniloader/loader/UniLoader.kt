package xyz.unifycraft.uniloader.loader

import xyz.unifycraft.uniloader.loader.impl.UniLoaderImpl
import java.io.File

interface UniLoader {
    fun <T> getEntrypoints(namespace: String, type: Class<T>): List<T>
    fun isModLoaded(id: String): Boolean
    fun isDevelopmentEnvironment(): Boolean

    fun getGameDir(): File
    fun getConfigDir(): File
    fun getModsDir(): File

    fun setBridge(bridge: MinecraftBridge)

    companion object {
        private lateinit var instance: UniLoader

        @JvmStatic
        fun getInstance(): UniLoader {
            if (!::instance.isInitialized) {
                val className = System.getProperty("uniloader.class", System.getenv("UNILOADER_CLASS"))
                instance = if (className != null) {
                    val clz = Class.forName(className)
                    clz.getConstructor().newInstance() as UniLoader
                } else UniLoaderImpl()
            }
            return instance
        }
    }
}
