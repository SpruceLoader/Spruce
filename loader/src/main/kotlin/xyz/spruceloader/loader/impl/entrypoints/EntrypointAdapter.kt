package xyz.spruceloader.loader.impl.entrypoints

import xyz.spruceloader.api.Entrypoint

abstract class EntrypointAdapter {
    abstract fun <T : Entrypoint> create(entry: String, type: Class<T>): T
}
