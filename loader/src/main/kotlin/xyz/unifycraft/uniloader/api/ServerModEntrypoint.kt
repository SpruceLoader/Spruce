package xyz.unifycraft.uniloader.api

/**
 * The main class for mods supporting only the
 * server-side. This includes both dedicated and
 * integrated servers.
 *
 * This entrypoint is defined with the `server`
 * key in your mod's `mod.metadata.json` file.
 *
 * @see CommonModEntrypoint
 * @see ClientModEntrypoint
 */
interface ServerModEntrypoint : CommonModEntrypoint
