package xyz.spruceloader.loader.impl.transform

/*import org.objectweb.asm.tree.ClassNode
import xyz.spruceloader.api.TransformerEntrypoint
import xyz.spruceloader.loader.api.SpruceLoader
import xyz.spruceloader.ulasm.transformers.BaseTransformer
import xyz.spruceloader.ulasm.transformers.InitialReadResult

object TransformerEntrypointTransformer : BaseTransformer {
    private val isLoaded: Boolean
        get() = SpruceLoader.getInstance().isLoadingComplete
    private val entrypoints: List<TransformerEntrypoint>
        get() = SpruceLoader.getInstance().getEntrypoints("transformer")

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
}*/
