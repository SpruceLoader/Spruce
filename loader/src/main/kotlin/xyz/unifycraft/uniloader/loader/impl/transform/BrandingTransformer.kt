package xyz.unifycraft.uniloader.loader.impl.transform

import org.apache.logging.log4j.LogManager
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.tree.MethodInsnNode
import xyz.unifycraft.uniloader.loader.api.UniLoader

object BrandingTransformer {
    private val logger = LogManager.getLogger()
    private const val VANILLA = "vanilla"

    fun transform(node: ClassNode) {
        for (method in node.methods) {
            if ((method.name.equals("getClientModName") || method.name.equals("getServerModName")) && method.desc.endsWith(")Ljava/lang/String;")) {
                val iterator = method.instructions.iterator()
                while (iterator.hasNext()) {
                    if (iterator.next().opcode != Opcodes.ARETURN) continue
                    iterator.previous()
                    iterator.add(MethodInsnNode(Opcodes.INVOKESTATIC, BrandingTransformer::class.java.name, "fetchBranding", "(Ljava/lang/String;)Ljava/lang/String;", false))
                    iterator.next()
                }
            }
        }
    }

    @Suppress("unused")
    fun fetchBranding(original: String?): String {
        if (original.isNullOrBlank()) {
            logger.warn("Original brand name was either empty or null!")
            return UniLoader.NAME
        }

        return if (VANILLA.equals(original)) UniLoader.NAME else "$original,${UniLoader.NAME}"
    }
}
