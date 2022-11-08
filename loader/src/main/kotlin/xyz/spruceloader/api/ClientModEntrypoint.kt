package xyz.spruceloader.api

/**
 * The main class for mods supporting exclusively
 * the client-side.
 *
 * This entrypoint is defined with the `client`
 * key in your mod's `mod.metadata.json` file.
 *
 * @see PreLaunchEntrypoint
 * @see CommonModEntrypoint
 * @see ServerModEntrypoint
 */
interface ClientModEntrypoint : CommonModEntrypoint
