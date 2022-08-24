package xyz.unifycraft.uniloader.loader.impl

import xyz.unifycraft.uniloader.loader.MinecraftBridge
import java.io.File

class MinecraftBridgeImpl(
    private val launchArgs: Map<String, String>
) : MinecraftBridge {
    override fun getLaunchDir() =
        File(if (launchArgs.containsKey("gameDir")) launchArgs["gameDir"]!! else ".")
    override fun isObfuscated() =
        true
    override fun getLaunchArgs() =
        launchArgs
}
