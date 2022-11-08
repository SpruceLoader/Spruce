package xyz.spruceloader.loader.impl.transform

/*import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.ClassNode
import xyz.spruceloader.ulasm.transformers.BaseTransformer
import xyz.spruceloader.ulasm.utils.InsnHelper
import xyz.spruceloader.ulasm.utils.InvokeType

object BrandingTransformer : BaseTransformer {
    const val VANILLA = "vanilla"

    override fun getTargets() = listOf(
        "net.minecraft.client.ClientBrandRetriever",
        "net.minecraft.server.MinecraftServer"
    )

    override fun transform(node: ClassNode): Boolean {
        var modified = false

        for (method in node.methods) {
            if ((method.name.equals("getClientModName") || method.name.equals("getServerModName")) && method.desc.endsWith(")Ljava/lang/String;")) {
                val iterator = method.instructions.iterator()
                while (iterator.hasNext()) {
                    if (iterator.next().opcode == Opcodes.ARETURN) {
                        iterator.previous()
                        iterator.add(InsnHelper.method(InvokeType.STATIC, Hooks::class.java, "fetchBranding", "(Ljava/lang/String;)Ljava/lang/String;"))
                        iterator.next()
                        modified = true
                    }
                }
            }
        }

        return modified
    }
}*/
