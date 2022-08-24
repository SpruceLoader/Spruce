package xyz.unifycraft.uniloader.api

/**
 * The main class for mods supporting exclusively
 * the client-side.
 *
 * This entrypoint is defined with the `client`
 * key in your mod's `mod.metadata.json` file.
 *
 * @see ClientModEntrypoint
 * @see ServerModEntrypoint
 */
interface ClientModEntrypoint : CommonModEntrypoint
