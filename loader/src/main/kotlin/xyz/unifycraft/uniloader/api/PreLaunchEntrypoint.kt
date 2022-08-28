package xyz.unifycraft.uniloader.api

import xyz.unifycraft.uniloader.loader.ArgumentMap

/**
 * An entrypoint interface which allows mods to
 * run code during the game's initial start-up
 * phase. DO NOT call internal Minecraft classes
 * from here unless you explicitly know what you're
 * doing!
 *
 * This entrypoint is defined with the `preLaunch`
 * key in your mod's `mod.metadata.json` file.
 *
 * @see CommonModEntrypoint
 * @see ClientModEntrypoint
 * @see ServerModEntrypoint
 */
interface PreLaunchEntrypoint {
    fun initialize(arguments: ArgumentMap, classLoader: ClassLoader)
}
