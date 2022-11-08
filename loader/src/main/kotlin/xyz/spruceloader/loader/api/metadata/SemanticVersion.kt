package xyz.spruceloader.loader.api.metadata

interface SemanticVersion : Version {
    val major: Int
    val minor: Int
    val patch: Int
    val prerelease: PrereleaseType?
    val buildMetadata: String?

    enum class PrereleaseType(
        val suffix: String
    ) : Comparable<PrereleaseType> {
        ALPHA("alpha"),
        BETA("beta"),
        RELEASE_CANDIDATE("rc"),
        PRERELEASE("prerelease"),
        RELEASE("");

        fun isOf(vararg other: PrereleaseType) =
            this in other

        fun compare(other: PrereleaseType): Int {
            if (this == RELEASE && other != RELEASE)
                return -1
            if (this == PRERELEASE && !other.isOf(RELEASE, PRERELEASE))
                return -1

            return 0
        }

        override fun toString() = suffix

        companion object {
            @JvmStatic
            fun from(input: String?) = if (input == null) null else values().firstOrNull {
                it.name.contains(input, true)
            }
        }
    }
}
