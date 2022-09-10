package xyz.unifycraft.uniloader.loader.impl.metadata.version

import xyz.unifycraft.uniloader.loader.impl.metadata.Version

class SemanticVersion(
    private val major: Int,
    private val minor: Int,
    private val patch: Int,
    private val channel: String?,
    private val build: Int?
) : Version {
    companion object : Version.VersionVerifier {
        @JvmStatic val regex = "(?<major>[0-9]+)\\.(?<minor>[0-9]+)\\.(?<patch>[0-9]+)(?:-(?<channel>[0-9A-Za-z-]+))?(?:\\+(?<build>[0-9A-Za-z-]+))?".toRegex()

        override fun isValid(input: String) = input.matches(regex)
        override fun create(input: String): Version {
            val match = regex.find(input) ?: throw IllegalArgumentException("Could find semantic version from \"$input\"!")
            val groups = match.groups as MatchNamedGroupCollection
            return SemanticVersion(
                groups["major"]!!.value.toInt(),
                groups["minor"]!!.value.toInt(),
                groups["patch"]!!.value.toInt(),
                groups["channel"]?.value,
                groups["build"]?.value?.toIntOrNull()
            )
        }
    }

    private val channelType = ChannelType.values().firstOrNull {
        it.suffixes.contains(channel)
    } ?: ChannelType.RELEASE

    override fun getReadableString() = buildString {
        append(major).append(".")
        append(minor).append(".")
        append(patch)
        if (channel != null) append("-").append(channel)
        if (build != null) append("+").append(build)
    }

    override fun compareTo(other: Version): Int {
        if (other !is SemanticVersion)
            return getReadableString().compareTo(other.getReadableString())

        val split = getReadableString().split(".")
        val otherSplit = other.getReadableString().split(".")
        var compared = -100

        for (i in 1..3) {
            val v1 = split[i].toInt()
            val v2 = otherSplit[i].toInt()
            val comparison = v1.compareTo(v2)
            if (compared != 0) {
                compared = comparison
                break
            }
        }

        if (compared == 0) {
            compared = channelType.compareTo(other.channelType)
            if (build != null && other.build != null) compared = build.compareTo(other.build)
        }

        return compared
    }

    private enum class ChannelType(
        vararg val suffixes: String
    ) : Comparable<ChannelType> {
        ALPHA("alpha"),
        BETA("beta"),
        RELEASE_CANDIDATE("rc", "releasecandidate"),
        PRERELEASE("prerelease"),
        RELEASE("");

        fun isOf(vararg other: ChannelType): Boolean {
            for (type in other) {
                if (this == type) {
                    return true
                }
            }

            return false
        }

        fun compare(other: ChannelType): Int {
            if (this == RELEASE && other != RELEASE)
                return -1
            if (this == PRERELEASE && !other.isOf(RELEASE, PRERELEASE))
                return -1

            return 0
        }
    }
}
