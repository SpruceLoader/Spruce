package xyz.unifycraft.uniloader.loader

import java.io.File

interface MinecraftBridge {
    fun getLaunchDir(): File
    fun isObfuscated(): Boolean

    fun getLaunchArgs(): Map<String, String>
}
