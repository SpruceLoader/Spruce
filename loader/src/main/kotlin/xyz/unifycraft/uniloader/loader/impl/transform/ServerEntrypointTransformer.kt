package xyz.unifycraft.uniloader.loader.impl.transform

import org.apache.logging.log4j.LogManager
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.ClassNode

object ServerEntrypointTransformer {
    private val logger = LogManager.getLogger()

    fun transform(node: ClassNode) {
        for (method in node.methods) {
            if (method.name != "main") continue

            val iterator = method.instructions.iterator()
            while (iterator.hasNext()) {
                val insn = iterator.next()
                if (insn.opcode != Opcodes.NEW) continue

                // TODO
            }
        }
    }
}
