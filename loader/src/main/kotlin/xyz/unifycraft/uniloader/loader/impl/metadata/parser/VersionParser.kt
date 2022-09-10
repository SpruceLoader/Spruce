package xyz.unifycraft.uniloader.loader.impl.metadata.parser

import xyz.unifycraft.uniloader.loader.exceptions.VersionParsingException
import xyz.unifycraft.uniloader.loader.impl.metadata.Version
import xyz.unifycraft.uniloader.loader.impl.metadata.version.BuildVersion
import xyz.unifycraft.uniloader.loader.impl.metadata.version.SemanticVersion
import xyz.unifycraft.uniloader.loader.impl.metadata.version.StringVersion
import kotlin.jvm.Throws

object VersionParser {
    private val versions = listOf(
        SemanticVersion,
        BuildVersion,
        StringVersion
    )

    @JvmStatic
    @Throws(VersionParsingException::class)
    fun parse(input: String): Version {
        val version = versions.first { it.isValid(input) }
        return version.create(input)
    }
}
