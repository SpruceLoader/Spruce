package xyz.unifycraft.uniloader.loader.impl.entrypoints

import xyz.unifycraft.launchwrapper.Launch
import xyz.unifycraft.uniloader.api.Entrypoint
import xyz.unifycraft.uniloader.loader.api.UniLoader
import xyz.unifycraft.uniloader.loader.impl.discoverer.ModDiscoverer
import xyz.unifycraft.uniloader.loader.impl.metadata.EntrypointMetadata

// TODO - Improve... This is pretty bad
object EntrypointHandler {
    private val metadata = mutableMapOf<String, EntrypointMetadata>()
    private val adapterCache = mutableMapOf<String, EntrypointAdapter>()
    private val cache = mutableMapOf<String, MutableList<Entrypoint>>()

    private val classLoader: ClassLoader
        get() = Launch.getInstance().classLoader

    fun initialize(discoverer: ModDiscoverer) {
        discoverer.getMods().forEach { mod ->
            mod.loader?.entrypoints?.forEach { (key, metadata) ->
                EntrypointHandler.metadata[key] = metadata
            }
        }

        cacheEntrypoints()
    }

    fun cacheEntrypoints() {
        metadata.forEach { (key, metadata) ->
            val obj = if (metadata.adapter != null) {
                val adapter = adapterCache.computeIfAbsent(metadata.adapter) { name ->
                    val clz = Class.forName(name)
                    if (!clz.superclass.isAssignableFrom(EntrypointAdapter::class.java))
                        throw IllegalArgumentException("The class provided as an adapter is not valid! (${metadata.adapter})")
                    clz.getConstructor().newInstance() as EntrypointAdapter
                }
                val clz = classLoader.loadClass(metadata.value)
                if (!clz.superclass.isAssignableFrom(Entrypoint::class.java))
                    throw IllegalArgumentException("The class provided as an entrypoint is not valid! (${metadata.value})")
                adapter.create(metadata.value, clz as Class<Entrypoint>)
            } else classLoader.loadClass(metadata.value).getConstructor().newInstance() as Entrypoint
            val entrypoints = cache.computeIfAbsent(key) { mutableListOf() }
            entrypoints.add(obj)
            cache[key] = entrypoints
        }
    }

    fun getEntrypoints(key: String) = cache[key]?.toList()
}
