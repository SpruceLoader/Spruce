package xyz.unifycraft.uniloader.loader.impl

import org.apache.logging.log4j.LogManager
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.tree.ClassNode
import xyz.unifycraft.launchwrapper.Launch
import xyz.unifycraft.launchwrapper.api.ArgumentMap
import xyz.unifycraft.launchwrapper.api.EnvSide
import xyz.unifycraft.launchwrapper.api.LaunchTransformer
import xyz.unifycraft.uniloader.api.TransformerEntrypoint
import xyz.unifycraft.uniloader.loader.api.Properties
import xyz.unifycraft.uniloader.loader.api.UniLoader
import xyz.unifycraft.uniloader.loader.impl.transform.BrandingTransformer
import java.io.File
import java.io.FileOutputStream

class LoaderLaunchTransformer : LaunchTransformer {
    companion object {
        private val logger = LogManager.getLogger()
    }

    override fun takeArguments(argMap: ArgumentMap, env: EnvSide) {
        Launch.getInstance().classLoader.addClassLoaderException("xyz.unifycraft.uniloader.")

        val loader = UniLoader.getInstance()
        loader.load(argMap)
    }

    override fun transform(className: String, rawClass: ByteArray): ByteArray {
        var modified = false

       val classReader = ClassReader(rawClass)
        val classWriter = ClassWriter(ClassWriter.COMPUTE_FRAMES or ClassWriter.COMPUTE_MAXS)
        val classNode = ClassNode()
        classReader.accept(classNode, ClassReader.EXPAND_FRAMES)

        when (className) {
            "net.minecraft.client.ClientBrandRetriever",
            "net.minecraft.server.MinecraftServer" -> {
                modified = true
                BrandingTransformer.transform(classNode)
            }
        }

        if (UniLoader.getInstance().isLoadingComplete()) {
            for (entrypoint in UniLoader.getInstance().getEntrypoints("transformer", TransformerEntrypoint::class.java)) {
                val madeModification = entrypoint.performTransform(className, classNode)
                if (!modified) modified = madeModification
            }
        }

        classNode.accept(classWriter)

        if (modified && (System.getProperty(Properties.Debug.TRANSFORM_DEBUG, "false").toBooleanStrictOrNull() == true)) {
            val dir = File(UniLoader.getInstance().getLoaderDir(), "bytecode")
            dir.mkdirs()
            val fixedClassName =
                "${if (className.contains("$")) className.replace('$', '.') else className}.class"
            logger.warn("Writing $fixedClassName to ${dir.absolutePath} for debug purposes")
            val file = File(dir, fixedClassName)
            FileOutputStream(file).use { outputStream ->
                outputStream.write(classWriter.toByteArray())
            }
        }

        return classWriter.toByteArray()
    }
}
