package xyz.unifycraft.uniloader.loader.api.metadata

interface Version : Comparable<Version> {
    /**
     * @return A user-readable representation of this version.
     */
    val readableString: String

    /**
     * Version's companion objects should implement this so that
     * it's easier to parse and identifier versions.
     *
     * This method is not compatible with Java, because "custom"
     * version implementations are not allowed.
     */
    interface VersionVerifier {
        /**
         * @return Whether the input is a valid format for this version
         * or not.
         */
        fun isValid(input: String): Boolean
        fun create(input: String): Version
    }
}