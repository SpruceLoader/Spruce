package xyz.spruceloader.api

/**
 * The main class for mods supporting both the
 * server-side and client-side.
 *
 * This entrypoint is defined with the `common`
 * key in your mod's `mod.metadata.json` file.
 *
 * @see PreLaunchEntrypoint
 * @see ClientModEntrypoint
 * @see ServerModEntrypoint
 */
interface CommonModEntrypoint : Entrypoint {
    fun initialize()
}
