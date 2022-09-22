package xyz.unifycraft.uniloader.loader.impl.metadata.version

import xyz.unifycraft.uniloader.loader.api.metadata.BuildVersion
import xyz.unifycraft.uniloader.loader.api.metadata.Version
import xyz.unifycraft.uniloader.loader.exceptions.VersionParsingException

class BuildVersionImpl(
    override val build: Int,
    override val metadata: String?
) : BuildVersion {
    companion object : Version.VersionVerifier {
        @JvmStatic
        val regex = "(?<build>[0-9]+)(?:\\+(?<metadata>[a-zA-Z0-9]+))?".toRegex()

        override fun isValid(input: String) = input.matches(regex)
        override fun create(input: String): Version {
            val match = regex.find(input) ?: throw VersionParsingException("Could find build version from \"$input\"!")
            val groups = match.groups
            if (groups !is MatchNamedGroupCollection)
                throw VersionParsingException("Couldn't match to a named group collection... Weird?")

            return BuildVersionImpl(
                groups["build"]!!.value.toInt(),
                groups["metadata"]?.value
            )
        }
    }

    override val readableString = buildString {
        append(build)
        if (metadata != null) append("+").append(metadata)
    }

    override fun compareTo(other: Version): Int {
        if (other !is BuildVersion)
            return 0

        return (build - other.build).coerceIn(-1, 1)
    }
}
