package xyz.unifycraft.uniloader.api

import org.objectweb.asm.tree.ClassNode
import xyz.unifycraft.uniloader.ulasm.transformers.InitialReadResult

interface TransformerEntrypoint : Entrypoint {
    /**
     * @return The result of this read. This consists of whether a transformation
     * was made or not, and the raw class bytes.
     */
    fun beforeRead(bytes: ByteArray): InitialReadResult
    /**
     * @return Whether this method made a transformation or not. This
     * is optional, as it's only used to decide whether to write the transformed
     * bytecode to a file if the debug property is present.
     */
    fun performTransform(classNode: ClassNode): Boolean
}
