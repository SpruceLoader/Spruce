package xyz.unifycraft.uniloader.loader.impl

import xyz.unifycraft.uniloader.loader.MinecraftBridge
import xyz.unifycraft.uniloader.loader.UniLoader
import java.io.File
import java.lang.IllegalStateException

class UniLoaderImpl : UniLoader {
    private lateinit var bridge: MinecraftBridge

    override fun <T> getEntrypoints(namespace: String, type: Class<T>): List<T> {
        TODO("Not yet implemented")
    }

    override fun isModLoaded(id: String): Boolean {
        return true
    }

    override fun isDevelopmentEnvironment(): Boolean {
        TODO("Not yet implemented")
    }

    override fun getGameDir() = bridge.getLaunchDir()
    override fun getConfigDir() = File(bridge.getLaunchDir(), "config")
    override fun getModsDir() = File(bridge.getLaunchDir(), "mods")

    override fun setBridge(bridge: MinecraftBridge) {
        if (::bridge.isInitialized) throw IllegalStateException("The MinecraftBridge has already been set!")
        this.bridge = bridge
    }

    companion object {
        @JvmStatic
        fun getInstance() {

        }
    }
}
