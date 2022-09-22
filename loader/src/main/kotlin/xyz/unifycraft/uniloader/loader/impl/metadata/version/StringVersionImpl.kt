package xyz.unifycraft.uniloader.loader.impl.metadata.version

import xyz.unifycraft.uniloader.loader.api.metadata.Version

class StringVersionImpl(
    val input: String
) : Version {
    companion object : Version.VersionVerifier {
        override fun isValid(input: String) = true
        override fun create(input: String) = StringVersionImpl(input)
    }

    override val readableString = input
    override fun compareTo(other: Version) =
        readableString.compareTo(other.readableString)
}
