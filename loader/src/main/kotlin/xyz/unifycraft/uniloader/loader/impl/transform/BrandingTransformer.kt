package xyz.unifycraft.uniloader.loader.impl.transform

import org.apache.logging.log4j.LogManager
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.tree.MethodInsnNode

object BrandingTransformer {
    private val logger = LogManager.getLogger()
    const val VANILLA = "vanilla"

    fun transform(node: ClassNode) {
        for (method in node.methods) {
            if (method.name.equals("getClientModName") || method.name.equals("getServerModName") && method.desc.endsWith(")Ljava/lang/String;")) {
                logger.info("Applying branding modification to ${method.name} in ${node.name}")
                val iterator = method.instructions.iterator()
                while (iterator.hasNext()) {
                    if (iterator.next().opcode == Opcodes.ARETURN) {
                        iterator.previous()
                        iterator.add(MethodInsnNode(Opcodes.INVOKESTATIC, Hooks.internalName, "fetchBranding", "(Ljava/lang/String;)Ljava/lang/String;", false))
                        iterator.next()
                    }
                }
            }
        }
    }
}
