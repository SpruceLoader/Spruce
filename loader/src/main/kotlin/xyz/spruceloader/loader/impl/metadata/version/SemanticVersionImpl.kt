package xyz.spruceloader.loader.impl.metadata.version

import xyz.spruceloader.loader.api.metadata.SemanticVersion
import xyz.spruceloader.loader.api.metadata.Version
import xyz.spruceloader.loader.exceptions.VersionParsingException

class SemanticVersionImpl(
    override val major: Int,
    override val minor: Int,
    override val patch: Int,
    override val prerelease: SemanticVersion.PrereleaseType?,
    override val buildMetadata: String?
) : SemanticVersion {
    companion object : Version.VersionVerifier {
        @JvmStatic
        val regex = "(?<major>0|[1-9]\\d*)\\.(?<minor>0|[1-9]\\d*)(?:\\.(?<patch>0|[1-9]\\d*))?(?:-(?<prerelease>(?:0|[1-9]\\d*|\\d*[a-zA-Z-][0-9a-zA-Z-]*)(?:\\.(?:0|[1-9]\\d*|\\d*[a-zA-Z-][0-9a-zA-Z-]*))*))?(?:\\+(?<buildmetadata>[0-9a-zA-Z-]+(?:\\.[0-9a-zA-Z-]+)*))?".toRegex()

        override fun isValid(input: String) = input.matches(regex)
        override fun create(input: String): Version {
            val match = regex.find(input) ?: throw VersionParsingException("Could find semantic version from \"$input\"!")
            val groups = match.groups
            if (groups !is MatchNamedGroupCollection)
                throw VersionParsingException("Couldn't match to a named group collection... Weird?")

            return SemanticVersionImpl(
                groups["major"]!!.value.toInt(),
                groups["minor"]!!.value.toInt(),
                groups["patch"]?.value?.toInt() ?: 0,
                SemanticVersion.PrereleaseType.from(groups["prerelease"]?.value),
                groups["buildmetadata"]?.value
            )
        }
    }

    override val readableString = buildString {
        append(major).append(".")
        append(minor).append(".")
        append(patch)
        if (prerelease != null) append("-").append(prerelease)
        if (buildMetadata != null) append("+").append(buildMetadata)

    }

    override fun compareTo(other: Version): Int {
        if (other !is SemanticVersion)
            return 0

        val split = readableString.split(".")
        val otherSplit = other.readableString.split(".")
        var compared = -100

        for (i in 1..3) {
            val v1 = split[i].toInt()
            val v2 = otherSplit[i].toInt()
            if (compared != 0) {
                compared = v1.compareTo(v2)
                break
            }
        }

        if (compared == 0 && prerelease != null && other.prerelease != null)
            compared = prerelease.compareTo(other.prerelease!!)

        return compared
    }
}
