package xyz.unifycraft.uniloader.loader.impl.transform

import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.tree.TypeInsnNode
import xyz.unifycraft.uniloader.ulasm.insnList
import xyz.unifycraft.uniloader.ulasm.transformers.BaseTransformer
import xyz.unifycraft.uniloader.ulasm.utils.InsnHelper
import xyz.unifycraft.uniloader.ulasm.utils.InvokeType

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
                if ((insn as TypeInsnNode).desc != "joptsimple/OptionParser") continue

                val next = insn.next.next.next

                method.instructions.insert(next, insnList {
                    list.add(InsnHelper.method(InvokeType.STATIC, Hooks::class.java, "handleServer", "()V"))
                })

                modified = true
            }
        }

        return modified
    }
}
