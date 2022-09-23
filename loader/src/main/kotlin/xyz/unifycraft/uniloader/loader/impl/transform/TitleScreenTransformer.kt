package xyz.unifycraft.uniloader.loader.impl.transform

import org.objectweb.asm.Handle
import org.objectweb.asm.Type
import org.objectweb.asm.tree.*
import xyz.unifycraft.uniloader.loader.api.UniLoader
import xyz.unifycraft.uniloader.ulasm.insnList
import xyz.unifycraft.uniloader.ulasm.transformers.BaseTransformer
import xyz.unifycraft.uniloader.ulasm.utils.InsnHelper
import xyz.unifycraft.uniloader.ulasm.utils.InvokeType

object TitleScreenTransformer : BaseTransformer {

    override fun getTarget() = "net.minecraft.client.gui.screen.TitleScreen"

    override fun transform(node: ClassNode): Boolean {
        var modified = false

        val syncOpenWebsiteMethod = MethodNode(InsnHelper.ACC_PRIVATE + InsnHelper.ACC_SYNTHETIC, "syncOpenWebsite","(Lnet/minecraft/client/gui/widget/ButtonWidget;)V",null,null)

        val syncInsc = syncOpenWebsiteMethod.instructions

        syncInsc.add(insnList {
            list.add(InsnHelper.method(InvokeType.STATIC, Hooks::class.java, "openWebsite", "()V"))
            list.add(InsnNode(InsnHelper.RETURN))
        })

        node.methods.add(syncOpenWebsiteMethod)

        for (method in node.methods) {
            if (method.name != "init") continue
            val iterator = method.instructions.iterator()
            while (iterator.hasNext()) {
                val insn = iterator.next()
                if (insn.opcode != InsnHelper.INVOKEVIRTUAL) continue
                if ((insn as MethodInsnNode).name != "setConnectedToRealms") continue

                val prev = insn.previous.previous.previous.previous

                method.instructions.insertBefore(prev, insnList {
                    list.add(VarInsnNode(InsnHelper.ALOAD, 0))
                    list.add(TypeInsnNode(InsnHelper.NEW, "net/minecraft/client/gui/widget/PressableTextWidget"))
                    list.add(InsnNode(InsnHelper.DUP))
                    list.add(VarInsnNode(InsnHelper.BIPUSH, 0))
                    list.add(VarInsnNode(InsnHelper.ALOAD, 0))
                    list.add(FieldInsnNode(InsnHelper.GETFIELD, "net/minecraft/client/gui/screen/TitleScreen", "height", "I"))
                    list.add(VarInsnNode(InsnHelper.BIPUSH, 20))
                    list.add(InsnNode(InsnHelper.ISUB))
                    list.add(VarInsnNode(InsnHelper.ILOAD, 1))
                    list.add(VarInsnNode(InsnHelper.BIPUSH, 10))
                    list.add(LdcInsnNode("${UniLoader.NAME} ${UniLoader.VERSION} (${UniLoader.getInstance().allMods.size} Mods loaded)"))
                    list.add(MethodInsnNode(InsnHelper.INVOKESTATIC, "net/minecraft/text/Text", "literal", "(Ljava/lang/String;)Lnet/minecraft/text/MutableText;",true))
                    list.add(VarInsnNode(InsnHelper.ALOAD, 0))
                    list.add(
                        InvokeDynamicInsnNode(
                            "onPress",
                            "(Lnet/minecraft/client/gui/screen/TitleScreen;)Lnet/minecraft/client/gui/widget/ButtonWidget\$PressAction;",
                            Handle(
                                InsnHelper.H_INVOKESTATIC,
                                "java/lang/invoke/LambdaMetafactory",
                                "metafactory",
                                "(Ljava/lang/invoke/MethodHandles\$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodHandle;Ljava/lang/invoke/MethodType;)Ljava/lang/invoke/CallSite;",
                                false
                            ),
                            Type.getType("(Lnet/minecraft/client/gui/widget/ButtonWidget;)V"),
                            Handle(
                                InsnHelper.H_INVOKEVIRTUAL,
                                "net/minecraft/client/gui/screen/TitleScreen",
                                "syncOpenWebsite",
                                "(Lnet/minecraft/client/gui/widget/ButtonWidget;)V",
                                false
                            ),
                            Type.getType("(Lnet/minecraft/client/gui/widget/ButtonWidget;)V")
                        )
                    )
                    list.add(VarInsnNode(InsnHelper.ALOAD, 0))
                    list.add(
                        FieldInsnNode(
                            InsnHelper.GETFIELD,
                            "net/minecraft/client/gui/screen/TitleScreen",
                            "textRenderer",
                            "Lnet/minecraft/client/font/TextRenderer;"
                        )
                    )
                    list.add(
                        MethodInsnNode(
                            InsnHelper.INVOKESPECIAL,
                            "net/minecraft/client/gui/widget/PressableTextWidget",
                            "<init>",
                            "(IIIILnet/minecraft/text/Text;Lnet/minecraft/client/gui/widget/ButtonWidget\$PressAction;Lnet/minecraft/client/font/TextRenderer;)V"
                        )
                    )
                    list.add(
                        MethodInsnNode(
                            InsnHelper.INVOKEVIRTUAL,
                            "net/minecraft/client/gui/screen/TitleScreen",
                            "addDrawableChild",
                            "(Lnet/minecraft/client/gui/Element;)Lnet/minecraft/client/gui/Element;"
                        )
                    )
                    list.add(InsnNode(InsnHelper.POP))
                })

                modified = true
            }
        }

        return modified
    }
}