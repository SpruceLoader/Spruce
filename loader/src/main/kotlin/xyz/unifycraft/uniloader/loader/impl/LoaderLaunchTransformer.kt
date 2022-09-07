package xyz.unifycraft.uniloader.loader.impl

import xyz.unifycraft.launchwrapper.api.ArgumentMap
import xyz.unifycraft.launchwrapper.api.EnvSide
import xyz.unifycraft.launchwrapper.api.LaunchTransformer
import xyz.unifycraft.uniloader.loader.api.UniLoader

class LoaderLaunchTransformer : LaunchTransformer {
    override fun takeArguments(argMap: ArgumentMap, env: EnvSide) {
        val loader = UniLoader.getInstance()
        loader.load(argMap)
    }

    override fun transform(className: String, rawClass: ByteArray): ByteArray {
        return rawClass
    }
}
