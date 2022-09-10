package xyz.unifycraft.uniloader.loader.impl.metadata

import xyz.unifycraft.uniloader.api.Entrypoint
import xyz.unifycraft.uniloader.loader.api.Environment

data class LoaderData(
    val environment: Environment,
    val accessWideners: List<String>, // Can also be singular
    val mixins: List<String>, // Can also be singular
    val entrypoints: Map<String, EntrypointMetadata>,
    val dependencies: List<Dependency>
) {
    companion object {

        @JvmStatic
        fun empty() =
            LoaderData(Environment.BOTH, emptyList(), emptyList(), emptyMap(), emptyList())

    }
}
