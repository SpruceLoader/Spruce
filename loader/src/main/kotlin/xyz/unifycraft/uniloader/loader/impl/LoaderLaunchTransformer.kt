package xyz.unifycraft.uniloader.loader.impl

import xyz.unifycraft.launchwrapper.Launch
import xyz.unifycraft.launchwrapper.api.ArgumentMap
import xyz.unifycraft.launchwrapper.api.EnvSide
import xyz.unifycraft.launchwrapper.api.LaunchTransformer
import xyz.unifycraft.uniloader.loader.api.UniLoader
import xyz.unifycraft.uniloader.loader.impl.transform.*
import xyz.unifycraft.uniloader.ulasm.ULASM
import xyz.unifycraft.uniloader.ulasm.transformers.BaseTransformer
import xyz.unifycraft.uniloader.ulasm.transformers.Transformers
import java.io.File

class LoaderLaunchTransformer : LaunchTransformer {
    companion object {
        private val transformers = mutableListOf<BaseTransformer>()
        private val bytecodeDir by lazy {
            File(UniLoader.getInstance().loaderDir, "bytecode")
        }

        init {
            transformers.add(BrandingTransformer)
            transformers.add(TitleScreenTransformer)
            transformers.add(ClientEntrypointTransformer)
            transformers.add(ServerEntrypointTransformer)
            transformers.add(TransformerEntrypointTransformer)
        }
    }

    override fun takeArguments(argMap: ArgumentMap, env: EnvSide) {
        Launch.getInstance().classLoader.addClassLoaderException("xyz.unifycraft.uniloader.")

        val loader = UniLoader.getInstance()
        loader.load(argMap, env)
    }

    override fun transform(className: String, rawClass: ByteArray): ByteArray {
        val result = Transformers.invoke(className ,rawClass, transformers)
        ULASM.debug(bytecodeDir, result.isModified, className, result.bytes)
        return result.bytes
    }
}
