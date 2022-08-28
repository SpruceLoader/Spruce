package xyz.unifycraft.uniloader.loader.impl

import xyz.unifycraft.uniloader.loader.ArgumentMap
import xyz.unifycraft.uniloader.loader.MinecraftBridge
import java.io.File

class MinecraftBridgeImpl : MinecraftBridge {
    private lateinit var launchArgs: ArgumentMap
    private val launchDirectory by lazy {
        val launchArgs = getLaunchArgs()
        val path = if (launchArgs.containsName("gameDir")) launchArgs["gameDir"]!! else "."
        File(path)
    }

    override fun getLaunchDir() =
        launchDirectory
    override fun isObfuscated() =
        true
    override fun getLaunchArgs(): ArgumentMap =
        launchArgs
    override fun setLaunchArgs(launchArgs: ArgumentMap) {
        this.launchArgs = launchArgs
    }

    override fun launch(classLoader: ClassLoader, args: Array<String>) {
        val clz = classLoader.loadClass("net.minecraft.client.main.Main")
        val invoker = MinecraftBridgeMethod.getMainMethod(clz)
        invoker.invokeExact(args)
    }
}
