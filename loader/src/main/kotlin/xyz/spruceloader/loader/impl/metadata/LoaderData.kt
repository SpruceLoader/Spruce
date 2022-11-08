package xyz.spruceloader.loader.impl.metadata

import xyz.spruceloader.loader.api.Environment

data class LoaderData(
    val environment: Environment,
    val entrypoints: Map<String, EntrypointMetadata>,
    val dependencies: List<Dependency>
) {
    companion object {

        @JvmStatic
        fun empty() =
            LoaderData(Environment.BOTH, emptyMap(), emptyList())

    }
}
