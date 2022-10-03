package xyz.unifycraft.uniloader.loader.impl.transform

/*import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.tree.LdcInsnNode
import xyz.unifycraft.uniloader.ulasm.insnList
import xyz.unifycraft.uniloader.ulasm.transformers.BaseTransformer
import xyz.unifycraft.uniloader.ulasm.utils.InsnHelper
import xyz.unifycraft.uniloader.ulasm.utils.InvokeType

object ClientEntrypointTransformer : BaseTransformer {
    override fun getTarget() = "net.minecraft.client.MinecraftClient"
    override fun transform(node: ClassNode): Boolean {
        var modified = false

        for (method in node.methods) {
            if (!method.name.equals("<init>")) continue

            val iterator = method.instructions.iterator()
            while (iterator.hasNext()) {
                val insn = iterator.next()
                if (insn.opcode != Opcodes.LDC) continue

                val ldcNode = insn as LdcInsnNode
                val cst = ldcNode.cst?.toString()
                if (cst.isNullOrBlank() || !cst.startsWith("Backend")) continue

                val next = insn.next.next.next
                method.instructions.insertBefore(next, insnList {
                    list.add(InsnHelper.method(InvokeType.STATIC, Hooks::class.java, "handleClient", "()V"))
                })
                modified = true
            }
        }

        return modified
    }
}
*/