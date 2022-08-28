package xyz.unifycraft.uniloader.loader

import xyz.unifycraft.uniloader.loader.impl.MinecraftBridgeImpl
import xyz.unifycraft.uniloader.utils.AbstractionHelper
import java.io.File

interface MinecraftBridge {
    fun getLaunchDir(): File
    fun isObfuscated(): Boolean

    fun getLaunchArgs(): ArgumentMap
    fun setLaunchArgs(launchArgs: ArgumentMap)

    fun launch(classLoader: ClassLoader, args: Array<String>)

    companion object {
        private lateinit var instance: MinecraftBridge

        fun getInstance(): MinecraftBridge {
            if (!::instance.isInitialized) instance =
                AbstractionHelper.create(MinecraftBridgeImpl::class.java, "uniloader.bridge", true)
            return instance
        }
    }
}
