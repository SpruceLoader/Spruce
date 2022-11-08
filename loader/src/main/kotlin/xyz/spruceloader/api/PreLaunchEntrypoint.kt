package xyz.spruceloader.api

import xyz.spruceloader.launchwrapper.api.ArgumentMap

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
interface PreLaunchEntrypoint : Entrypoint {
    fun initialize(arguments: ArgumentMap)
}
