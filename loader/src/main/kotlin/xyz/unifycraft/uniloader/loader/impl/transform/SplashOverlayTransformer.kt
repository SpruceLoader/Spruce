package xyz.unifycraft.uniloader.loader.impl.transform

import org.objectweb.asm.tree.*
import xyz.unifycraft.uniloader.ulasm.insnList
import xyz.unifycraft.uniloader.ulasm.transformers.BaseTransformer
import xyz.unifycraft.uniloader.ulasm.utils.InsnHelper
import xyz.unifycraft.uniloader.ulasm.utils.InvokeType

object SplashOverlayTransformer : BaseTransformer {
    override fun getTarget() = "net.minecraft.client.gui.screen.SplashOverlay"

    override fun transform(node: ClassNode): Boolean {

        var modified = false

        val renderRamBarMethod = MethodNode(InsnHelper.ACC_PRIVATE,"renderRamBar","(Lnet/minecraft/client/util/math/MatrixStack;IIIIFF)V",null,null)

        //after writing this I want to kms - koxx12
        //Before modifying any code here look at the output first as it makes way more sense that way - koxx12

        renderRamBarMethod.instructions.add(insnList {
            list.add(VarInsnNode(InsnHelper.ILOAD,4))
            list.add(VarInsnNode(InsnHelper.ILOAD,2))
            list.add(InsnNode(InsnHelper.ISUB))
            list.add(InsnNode(InsnHelper.ICONST_2))
            list.add(InsnNode(InsnHelper.ISUB))
            list.add(InsnNode(InsnHelper.I2F))
            list.add(VarInsnNode(InsnHelper.FLOAD,7))
            list.add(InsnNode(InsnHelper.FMUL))
            list.add(MethodInsnNode(InsnHelper.INVOKESTATIC,"net/minecraft/util/math/MathHelper","ceil","(F)I"))
            list.add(VarInsnNode(InsnHelper.ISTORE,8))
            list.add(VarInsnNode(InsnHelper.FLOAD,6))
            list.add(LdcInsnNode(255.0F))
            list.add(InsnNode(InsnHelper.FMUL))
            list.add(MethodInsnNode(InsnHelper.INVOKESTATIC,"java/lang/Math","round","(F)I"))
            list.add(VarInsnNode(InsnHelper.ISTORE,9))
            list.add(VarInsnNode(InsnHelper.ILOAD,9))
            list.add(LdcInsnNode(255))
            list.add(LdcInsnNode(255))
            list.add(LdcInsnNode(255))
            list.add(MethodInsnNode(InsnHelper.INVOKESTATIC,"net/minecraft/util/math/ColorHelper\$Argb","getArgb","(IIII)I"))
            list.add(VarInsnNode(InsnHelper.ISTORE,10))
            list.add(VarInsnNode(InsnHelper.ALOAD,1))
            list.add(VarInsnNode(InsnHelper.ILOAD,2))
            list.add(InsnNode(InsnHelper.ICONST_2))
            list.add(InsnNode(InsnHelper.IADD))
            list.add(VarInsnNode(InsnHelper.ILOAD,3))
            list.add(InsnNode(InsnHelper.ICONST_2))
            list.add(InsnNode(InsnHelper.IADD))
            list.add(VarInsnNode(InsnHelper.ILOAD,2))
            list.add(VarInsnNode(InsnHelper.ILOAD,8))
            list.add(InsnNode(InsnHelper.IADD))
            list.add(VarInsnNode(InsnHelper.ILOAD,5))
            list.add(InsnNode(InsnHelper.ICONST_2))
            list.add(InsnNode(InsnHelper.ISUB))
            list.add(VarInsnNode(InsnHelper.ILOAD,9))
            list.add(VarInsnNode(InsnHelper.FLOAD,7))
            list.add(InsnHelper.method(InvokeType.STATIC,SplashOverlayTransformer::class.java,"getBarColor", "(IF)I"))
            list.add(MethodInsnNode(InsnHelper.INVOKESTATIC,"net/minecraft/client/gui/screen/SplashOverlay","fill","(Lnet/minecraft/client/util/math/MatrixStack;IIIII)V"))
            list.add(VarInsnNode(InsnHelper.ALOAD,1))
            list.add(VarInsnNode(InsnHelper.ILOAD,2))
            list.add(InsnNode(InsnHelper.ICONST_1))
            list.add(InsnNode(InsnHelper.IADD))
            list.add(VarInsnNode(InsnHelper.ILOAD,3))
            list.add(VarInsnNode(InsnHelper.ILOAD,4))
            list.add(InsnNode(InsnHelper.ICONST_1))
            list.add(InsnNode(InsnHelper.ISUB))
            list.add(VarInsnNode(InsnHelper.ILOAD,3))
            list.add(InsnNode(InsnHelper.ICONST_1))
            list.add(InsnNode(InsnHelper.IADD))
            list.add(VarInsnNode(InsnHelper.ILOAD,10))
            list.add(MethodInsnNode(InsnHelper.INVOKESTATIC,"net/minecraft/client/gui/screen/SplashOverlay","fill","(Lnet/minecraft/client/util/math/MatrixStack;IIIII)V"))
            list.add(VarInsnNode(InsnHelper.ALOAD,1))
            list.add(VarInsnNode(InsnHelper.ILOAD,2))
            list.add(InsnNode(InsnHelper.ICONST_1))
            list.add(InsnNode(InsnHelper.IADD))
            list.add(VarInsnNode(InsnHelper.ILOAD,5))
            list.add(VarInsnNode(InsnHelper.ILOAD,4))
            list.add(InsnNode(InsnHelper.ICONST_1))
            list.add(InsnNode(InsnHelper.ISUB))
            list.add(VarInsnNode(InsnHelper.ILOAD,5))
            list.add(InsnNode(InsnHelper.ICONST_1))
            list.add(InsnNode(InsnHelper.ISUB))
            list.add(VarInsnNode(InsnHelper.ILOAD,10))
            list.add(MethodInsnNode(InsnHelper.INVOKESTATIC,"net/minecraft/client/gui/screen/SplashOverlay","fill","(Lnet/minecraft/client/util/math/MatrixStack;IIIII)V"))
            list.add(VarInsnNode(InsnHelper.ALOAD,1))
            list.add(VarInsnNode(InsnHelper.ILOAD,2))
            list.add(VarInsnNode(InsnHelper.ILOAD,3))
            list.add(VarInsnNode(InsnHelper.ILOAD,2))
            list.add(InsnNode(InsnHelper.ICONST_1))
            list.add(InsnNode(InsnHelper.IADD))
            list.add(VarInsnNode(InsnHelper.ILOAD,5))
            list.add(VarInsnNode(InsnHelper.ILOAD,10))
            list.add(MethodInsnNode(InsnHelper.INVOKESTATIC,"net/minecraft/client/gui/screen/SplashOverlay","fill","(Lnet/minecraft/client/util/math/MatrixStack;IIIII)V"))
            list.add(VarInsnNode(InsnHelper.ALOAD,1))
            list.add(VarInsnNode(InsnHelper.ILOAD,4))
            list.add(VarInsnNode(InsnHelper.ILOAD,3))
            list.add(VarInsnNode(InsnHelper.ILOAD,4))
            list.add(InsnNode(InsnHelper.ICONST_1))
            list.add(InsnNode(InsnHelper.ISUB))
            list.add(VarInsnNode(InsnHelper.ILOAD,5))
            list.add(VarInsnNode(InsnHelper.ILOAD,10))
            list.add(MethodInsnNode(InsnHelper.INVOKESTATIC,"net/minecraft/client/gui/screen/SplashOverlay","fill","(Lnet/minecraft/client/util/math/MatrixStack;IIIII)V"))

            //text rendering code - koxx12

//            list.add(VarInsnNode(InsnHelper.ALOAD,1)) refrence to matrix stack
//            list.add(VarInsnNode(InsnHelper.ALOAD,0)) literary works as "this"
//            list.add(FieldInsnNode(InsnHelper.GETFIELD, "net/minecraft/client/gui/screen/SplashOverlay","client","Lnet/minecraft/client/MinecraftClient;")) gets "client" (this.client)
//            list.add(FieldInsnNode(InsnHelper.GETFIELD,"net/minecraft/client/MinecraftClient","textRenderer","Lnet/minecraft/client/font/TextRenderer;")) gets text renderer (this.client.textRenderer)
//            list.add(LdcInsnNode("RAM Usage: 1024MB/2048MB")) text that is supposed to be rendered
//            list.add(InsnNode(InsnHelper.ICONST_2)) pos x
//            list.add(InsnNode(InsnHelper.ICONST_2)) pos y
//            list.add(LdcInsnNode(255)) color a
//            list.add(LdcInsnNode(255)) color r
//            list.add(LdcInsnNode(255)) color g
//            list.add(LdcInsnNode(255)) color b
//            list.add(InsnHelper.method(InvokeType.STATIC,SplashOverlayTransformer::class.java,"getIntFromARGB","(IIII)I")) returns color as int
//
//            list.add(MethodInsnNode(InsnHelper.INVOKESTATIC, "net/minecraft/client/gui/screen/SplashOverlay","drawStringWithShadow","(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/font/TextRenderer;Ljava/lang/String;III)V")) finally draws text

            list.add(InsnNode(InsnHelper.RETURN))
        })

        node.methods.add(renderRamBarMethod)

        for (method in node.methods) {
            if (method.name != "render") continue
            val iterator = method.instructions.iterator()
            while (iterator.hasNext()) {
                val insn = iterator.next()
                if (insn.opcode != InsnHelper.INVOKEVIRTUAL) continue
                if ((insn as MethodInsnNode).name != "renderProgressBar") continue

                // i really hate asm and i want to kms - koxx12

                method.instructions.insert(insn, insnList {

                    list.add(VarInsnNode(InsnHelper.ALOAD,0))
                    list.add(VarInsnNode(InsnHelper.ALOAD,1))
                    list.add(VarInsnNode(InsnHelper.ILOAD,5))
                    list.add(InsnNode(InsnHelper.ICONST_2))
                    list.add(InsnNode(InsnHelper.IDIV))
                    list.add(VarInsnNode(InsnHelper.ILOAD,19))
                    list.add(InsnNode(InsnHelper.ISUB))

                    list.add(VarInsnNode(InsnHelper.ALOAD,0))
                    list.add(FieldInsnNode(InsnHelper.GETFIELD, "net/minecraft/client/gui/screen/SplashOverlay","client","Lnet/minecraft/client/MinecraftClient;"))
                    list.add(MethodInsnNode(InsnHelper.INVOKEVIRTUAL,"net/minecraft/client/MinecraftClient","getWindow","()Lnet/minecraft/client/util/Window;",false))
                    list.add(MethodInsnNode(InsnHelper.INVOKEVIRTUAL,"net/minecraft/client/util/Window","getScaledHeight","()I",false))
                    list.add(InsnHelper.method(InvokeType.STATIC,SplashOverlayTransformer::class.java,"calcHeight", "(I)I"))
                    list.add(InsnNode(InsnHelper.ICONST_5))
                    list.add(InsnNode(InsnHelper.ISUB))

                    list.add(VarInsnNode(InsnHelper.ILOAD,5))
                    list.add(InsnNode(InsnHelper.ICONST_2))
                    list.add(InsnNode(InsnHelper.IDIV))
                    list.add(VarInsnNode(InsnHelper.ILOAD,19))
                    list.add(InsnNode(InsnHelper.IADD))

                    list.add(VarInsnNode(InsnHelper.ALOAD,0))
                    list.add(FieldInsnNode(InsnHelper.GETFIELD, "net/minecraft/client/gui/screen/SplashOverlay","client","Lnet/minecraft/client/MinecraftClient;"))
                    list.add(MethodInsnNode(InsnHelper.INVOKEVIRTUAL,"net/minecraft/client/MinecraftClient","getWindow","()Lnet/minecraft/client/util/Window;",false))
                    list.add(MethodInsnNode(InsnHelper.INVOKEVIRTUAL,"net/minecraft/client/util/Window","getScaledHeight","()I",false))
                    list.add(InsnHelper.method(InvokeType.STATIC,SplashOverlayTransformer::class.java,"calcHeight", "(I)I"))
                    list.add(InsnNode(InsnHelper.ICONST_5))
                    list.add(InsnNode(InsnHelper.IADD))

                    list.add(InsnNode(InsnHelper.FCONST_1))
                    list.add(VarInsnNode(InsnHelper.FLOAD,9))
                    list.add(InsnNode(InsnHelper.FCONST_0))
                    list.add(InsnNode(InsnHelper.FCONST_1))
                    list.add(MethodInsnNode(InsnHelper.INVOKESTATIC,"net/minecraft/util/math/MathHelper","clamp","(FFF)F",false))
                    list.add(InsnNode(InsnHelper.FSUB))

                    list.add(InsnHelper.method(InvokeType.STATIC,SplashOverlayTransformer::class.java,"calcBarProgress", "()F"))

                    list.add(MethodInsnNode(InsnHelper.INVOKEVIRTUAL,"net/minecraft/client/gui/screen/SplashOverlay","renderRamBar","(Lnet/minecraft/client/util/math/MatrixStack;IIIIFF)V",false))
                })

                modified = true
            }
        }

        return modified
    }


    //its a pretty hacky solution but i had enough of working with the stack - koxx12
    @JvmStatic
    fun calcHeight(h: Int): Int {
        return (h.toDouble() * 0.1675).toInt()

    }

    @JvmStatic
    fun calcBarProgress(): Float {
        val max = Runtime.getRuntime().maxMemory()
        val total = Runtime.getRuntime().totalMemory()
        val free = Runtime.getRuntime().freeMemory()
        val o = total - free
        return (o.toFloat())/(max.toFloat())
    }

    @JvmStatic
    fun getBarColor(a: Int, p: Float): Int {

        //this only exists so no one gets confused what means what - koxx12

        val t = if (p > 0.45) {
            2
        } else if (p > 0.60) {
            3
        } else {
            1
        }

        //FIXME: These colors look kinda bad, change them before making any user builds - koxx12

        //1 = GOOD: rgb(57, 210, 34)
        //2 = DECENT: rgb(224, 154, 32)
        //3 = BAD (Might OOM): rgb(152, 32, 32)

        //ARGB

        return when (t) {
            1 -> getIntFromARGB(a,57,210,34)
            2 -> getIntFromARGB(a,224,154,32)
            3 -> getIntFromARGB(a,152,32,32)
            else -> getIntFromARGB(a,255,255,255)
        }
    }

    @JvmStatic
    fun getIntFromARGB(a: Int, r: Int, g: Int, b: Int): Int {
       return a shl 24 or (r shl 16) or (g shl 8) or b
    }

}