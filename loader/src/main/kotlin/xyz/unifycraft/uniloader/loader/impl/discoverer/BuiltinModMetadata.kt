package xyz.unifycraft.uniloader.loader.impl.discoverer

import com.google.gson.JsonObject
import xyz.unifycraft.uniloader.loader.api.Environment
import xyz.unifycraft.uniloader.loader.impl.metadata.*
import xyz.unifycraft.uniloader.loader.impl.metadata.parser.VersionParser

fun builtinMod(block: BuiltinModMetadata.() -> Unit) = BuiltinModMetadata()
    .apply(block)
    .build()

class BuiltinModMetadata {
    lateinit var name: String
    lateinit var version: String
    lateinit var id: String
    private var license: License? = null
    private val contributors = mutableListOf<Contributor>()
    private var links: ModLinks? = null

    fun license(block: LicenseScope.() -> Unit) = apply {
        this.license = LicenseScope()
            .apply(block)
            .build()
    }

    fun contributor(block: ContributorScope.() -> Unit) {
        this.contributors.add(ContributorScope()
            .apply(block)
            .build())
    }

    fun links(block: LinksScope.() -> Unit) {
        this.links = LinksScope()
            .apply(block)
            .build()
    }

    fun build() = ModMetadata(
        schemaVersion = ModMetadata.CURRENT_SCHEMA_VERSION,

        name = name,
        version = VersionParser.parse(version),
        id = id,
        type = listOf(ModType.LIBRARY),

        license = license,
        contributors = contributors,
        links = links,

        loader = LoaderData(
            environment = Environment.BOTH,
            entrypoints = emptyMap(),
            byteTransformConfigs = emptyList(),
            dependencies = emptyList()
        ),

        additional = JsonObject()
    )
}

class LicenseScope {
    lateinit var name: String
    var url: String? = null
    fun build() = License(name, url)
}

class ContributorScope {
    lateinit var name: String
    lateinit var role: String
    fun build() = Contributor(name, role)
}

class LinksScope {
    var home: String? = null
    var source: String? = null
    var issues: String? = null
    var discord: String? = null
    fun build() = ModLinks(home, source, issues, discord)
}
