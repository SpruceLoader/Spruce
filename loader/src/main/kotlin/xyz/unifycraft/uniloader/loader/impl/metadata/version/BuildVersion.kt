package xyz.unifycraft.uniloader.loader.impl.metadata.version

import xyz.unifycraft.uniloader.loader.impl.metadata.Version

class BuildVersion(
    private val input: Int
) : Version {
    companion object : Version.VersionVerifier {
        @JvmStatic val regex = "[0-9]*".toRegex()

        override fun isValid(input: String) = input.matches(regex)
        override fun create(input: String) = BuildVersion(input.toInt())
    }

    override fun getReadableString() = input.toString()
    override fun compareTo(other: Version): Int {
        if (other !is BuildVersion) return 0
        return (input - other.input).coerceIn(-1, 1)
    }
}
