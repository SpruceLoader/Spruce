package xyz.spruceloader.loader.impl.metadata.parser

import xyz.spruceloader.loader.api.metadata.Version
import xyz.spruceloader.loader.exceptions.VersionParsingException
import xyz.spruceloader.loader.impl.metadata.version.BuildVersionImpl
import xyz.spruceloader.loader.impl.metadata.version.SemanticVersionImpl
import xyz.spruceloader.loader.impl.metadata.version.StringVersionImpl
import kotlin.jvm.Throws

object VersionParser {
    private val versions = listOf(
        SemanticVersionImpl,
        BuildVersionImpl,
        StringVersionImpl
    )

    @JvmStatic
    @Throws(VersionParsingException::class)
    fun parse(input: String): Version {
        val version = versions.first { it.isValid(input) }
        return version.create(input)
    }
}
