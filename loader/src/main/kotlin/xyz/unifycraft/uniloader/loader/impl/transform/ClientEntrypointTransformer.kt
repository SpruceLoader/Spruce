package xyz.unifycraft.uniloader.loader.impl.transform

import org.apache.logging.log4j.LogManager
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.tree.InsnList
import org.objectweb.asm.tree.LdcInsnNode
import org.objectweb.asm.tree.MethodInsnNode

object ClientEntrypointTransformer {
    private val logger = LogManager.getLogger()

    fun transform(node: ClassNode) {
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
                println("next: $next")
                method.instructions.insertBefore(next, makeHookList())
                println("inserted")
            }
        }
    }

    private fun makeHookList(): InsnList {
        val list = InsnList()
        list.add(MethodInsnNode(Opcodes.INVOKESTATIC, Hooks.internalName, "handleClient", "()V", false))
        return list
    }
}
