package xyz.unifycraft.uniloader.loader.impl.transform

import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.ClassNode
import xyz.unifycraft.uniloader.ulasm.transformers.BaseTransformer

object ServerEntrypointTransformer : BaseTransformer {
    override fun getTargets() = listOf("net.minecraft.server.Main")
    override fun transform(node: ClassNode): Boolean {
        var modified = false

        for (method in node.methods) {
            if (method.name != "main") continue

            val iterator = method.instructions.iterator()
            while (iterator.hasNext()) {
                val insn = iterator.next()
                if (insn.opcode != Opcodes.NEW) continue

                // TODO: Implement Server Entrypoint
                modified = true
            }
        }

        return modified
    }
}
