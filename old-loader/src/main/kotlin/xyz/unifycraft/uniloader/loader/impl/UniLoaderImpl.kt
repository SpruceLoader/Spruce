package xyz.unifycraft.uniloader.loader.impl

import xyz.unifycraft.uniloader.loader.MinecraftBridge
import xyz.unifycraft.uniloader.loader.UniLoader
import xyz.unifycraft.uniloader.loader.impl.loading.ModClassLoader
import java.io.File

class UniLoaderImpl : UniLoader {
    private val bridge: MinecraftBridge
        get() = MinecraftBridge.getInstance()
    private lateinit var classLoader: ModClassLoader

    override fun <T> getEntrypoints(namespace: String, type: Class<T>): List<T> {
        return listOf()
    }

    override fun isModLoaded(id: String): Boolean {
        return true
    }

    override fun isDevelopmentEnvironment(): Boolean {
        TODO("Not yet implemented")
    }

    override fun getDataDir() = File(bridge.getLaunchDir(), "UniLoader")
    override fun getConfigDir() = File(getDataDir(), "config")
    override fun getModsDir() = File(getDataDir(), "mods")

    override fun getClassLoader() = classLoader
    override fun setClassLoader(classLoader: ModClassLoader) {
        this.classLoader = classLoader
    }
}
