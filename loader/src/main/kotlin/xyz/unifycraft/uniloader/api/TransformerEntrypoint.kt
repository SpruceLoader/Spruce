package xyz.unifycraft.uniloader.api

import org.objectweb.asm.tree.ClassNode

interface TransformerEntrypoint : Entrypoint {
    /**
     * @return Whether this method made a transformation or not. This
     * is optional, as it's only used to decide whether to write the transformed
     * bytecode to a file if the debug property is present.
     */
    fun performTransform(className: String, classNode: ClassNode): Boolean
}
