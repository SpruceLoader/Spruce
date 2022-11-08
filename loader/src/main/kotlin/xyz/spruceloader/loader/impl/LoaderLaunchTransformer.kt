package xyz.spruceloader.loader.impl

import org.spongepowered.asm.launch.MixinLaunchTransformer
import xyz.spruceloader.launchwrapper.Launch
import xyz.spruceloader.launchwrapper.LaunchClassLoader
import xyz.spruceloader.launchwrapper.api.ArgumentMap
import xyz.spruceloader.launchwrapper.api.EnvSide
import xyz.spruceloader.launchwrapper.api.LaunchTransformer
import xyz.spruceloader.loader.api.SpruceLoader

class LoaderLaunchTransformer : LaunchTransformer {
    companion object {
        /*
        init {
            transformers.add(BrandingTransformer)
            transformers.add(TitleScreenTransformer)
            transformers.add(ClientEntrypointTransformer)
            transformers.add(ServerEntrypointTransformer)
            transformers.add(TransformerEntrypointTransformer)
        }
        */
    }

    override fun takeArguments(argMap: ArgumentMap, env: EnvSide) {
        Launch.getInstance().classLoader.addClassLoaderException("xyz.spruceloader.")

        val loader = SpruceLoader.getInstance()
        loader.load(argMap, env)
    }

    override fun injectIntoClassLoader(classLoader: LaunchClassLoader) {
    }

    override fun getChildren() = listOf(
        MixinLaunchTransformer::class.java
    )
}
