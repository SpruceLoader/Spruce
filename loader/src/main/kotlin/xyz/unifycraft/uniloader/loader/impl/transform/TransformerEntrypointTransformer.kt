package xyz.unifycraft.uniloader.loader.impl.transform

/*import org.objectweb.asm.tree.ClassNode
import xyz.unifycraft.uniloader.api.TransformerEntrypoint
import xyz.unifycraft.uniloader.loader.api.UniLoader
import xyz.unifycraft.uniloader.ulasm.transformers.BaseTransformer
import xyz.unifycraft.uniloader.ulasm.transformers.InitialReadResult

object TransformerEntrypointTransformer : BaseTransformer {
    override fun getTarget() = "*"

    override fun beforeRead(bytes: ByteArray): InitialReadResult {
        var modified = false
        var bytes = bytes

        if (isLoaded) {
            for (entrypoint in entrypoints) {
                val result = entrypoint.beforeRead(bytes)
                bytes = result.bytes
                if (!modified) modified = result.isModified
            }
        }

        return if (modified)
            InitialReadResult.modified(bytes)
        else InitialReadResult.unmodified(bytes)
    }

    override fun transform(node: ClassNode): Boolean {
        var modified = false

        if (isLoaded) {
            for (entrypoint in entrypoints) {
                val madeModification = entrypoint.performTransform(node)
                if (!modified) modified = madeModification
            }
        }

        return modified
    }
}
*/