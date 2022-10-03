package xyz.unifycraft.uniloader.api

interface TransformerEntrypoint : Entrypoint {
    /**
     * @return The result of your modifications.
     */
    fun beforeByteTransform(classBytes: ByteArray): ByteArray
    /**
     * @return The result of your modifications.
     */
    fun afterByteTransform(classBytes: ByteArray): ByteArray
}
