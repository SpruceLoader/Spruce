package xyz.unifycraft.uniloader.loader.impl

import xyz.unifycraft.bytetransform.ByteTransform
import xyz.unifycraft.launchwrapper.Launch
import xyz.unifycraft.launchwrapper.api.ArgumentMap
import xyz.unifycraft.launchwrapper.api.EnvSide
import xyz.unifycraft.launchwrapper.api.LaunchTransformer
import xyz.unifycraft.uniloader.api.TransformerEntrypoint
import xyz.unifycraft.uniloader.loader.api.Environment
import xyz.unifycraft.uniloader.loader.api.UniLoader

class LoaderLaunchTransformer : LaunchTransformer {
    companion object {
        init {
            /*transformers.add(BrandingTransformer)
            transformers.add(TitleScreenTransformer)
            transformers.add(ClientEntrypointTransformer)
            transformers.add(ServerEntrypointTransformer)
            transformers.add(TransformerEntrypointTransformer)*/
        }
    }

    private val entrypoints: List<TransformerEntrypoint>
        get() = UniLoader.getInstance().getEntrypoints("transformer")
    private val isLoaded: Boolean
        get() = UniLoader.getInstance().isLoadingComplete

    override fun takeArguments(argMap: ArgumentMap, env: EnvSide) {
        Launch.getInstance().classLoader.addClassLoaderException("xyz.unifycraft.uniloader.")

        ByteTransform.setEnvironment(xyz.unifycraft.bytetransform.Environment.valueOf(env.name))
        ByteTransform.addConfigFile("bytetransform.uniloader.json")
        ByteTransform.initialize()

        val loader = UniLoader.getInstance()
        loader.environment = Environment.valueOf(env.name)
        loader.load(argMap)
    }

    override fun transform(className: String, rawClass: ByteArray): ByteArray {
        var classBytes = rawClass

        if (isLoaded) {
            for (entrypoint in entrypoints) {
                classBytes = entrypoint.beforeByteTransform(classBytes)
            }
        }

        classBytes = ByteTransform.handle(className, classBytes)

        if (isLoaded) {
            for (entrypoint in entrypoints) {
                classBytes = entrypoint.afterByteTransform(classBytes)
            }
        }

        return classBytes
    }
}
