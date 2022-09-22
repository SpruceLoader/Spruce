package xyz.unifycraft.uniloader.loader.impl

import xyz.unifycraft.launchwrapper.Launch
import xyz.unifycraft.launchwrapper.api.ArgumentMap
import xyz.unifycraft.launchwrapper.api.EnvSide
import xyz.unifycraft.launchwrapper.api.LaunchTransformer
import xyz.unifycraft.uniloader.loader.api.Environment
import xyz.unifycraft.uniloader.loader.api.UniLoader
import xyz.unifycraft.uniloader.loader.impl.transform.BrandingTransformer
import xyz.unifycraft.uniloader.loader.impl.transform.ClientEntrypointTransformer
import xyz.unifycraft.uniloader.loader.impl.transform.ServerEntrypointTransformer
import xyz.unifycraft.uniloader.loader.impl.transform.TransformerEntrypointTransformer
import xyz.unifycraft.uniloader.ulasm.ULASM
import xyz.unifycraft.uniloader.ulasm.transformers.BaseTransformer
import xyz.unifycraft.uniloader.ulasm.transformers.Transformers

class LoaderLaunchTransformer : LaunchTransformer {
    companion object {
        private val transformers = mutableListOf<BaseTransformer>()

        init {
            transformers.add(BrandingTransformer)
            transformers.add(ClientEntrypointTransformer)
            transformers.add(ServerEntrypointTransformer)
            transformers.add(TransformerEntrypointTransformer)
        }
    }

    override fun takeArguments(argMap: ArgumentMap, env: EnvSide) {
        Launch.getInstance().classLoader.addClassLoaderException("xyz.unifycraft.uniloader.")

        val loader = UniLoader.getInstance()
        loader.environment = Environment.valueOf(env.name)
        loader.load(argMap)
    }

    override fun transform(className: String, rawClass: ByteArray): ByteArray {
        val result = Transformers.invoke(rawClass, transformers)
        ULASM.debug(result.isModified, className, result.bytes)
        return result.bytes
    }
}
