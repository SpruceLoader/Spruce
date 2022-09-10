package xyz.unifycraft.uniloader.loader.impl.metadata.version

import xyz.unifycraft.uniloader.loader.impl.metadata.Version

class StringVersion(
    private val version: String
) : Version {
    companion object : Version.VersionVerifier {
        override fun isValid(input: String) = true
        override fun create(input: String) = StringVersion(input)
    }

    override fun getReadableString() = version
    override fun compareTo(other: Version): Int {
        if (other !is StringVersion)
            return version.compareTo(other.getReadableString())

        return version.compareTo(other.version)
    }
}
