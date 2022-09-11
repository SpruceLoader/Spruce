package xyz.unifycraft.uniloader.loader.impl.entrypoints

import xyz.unifycraft.uniloader.api.Entrypoint

abstract class EntrypointAdapter {
    abstract fun <T : Entrypoint> create(entry: String, type: Class<T>): T
}
