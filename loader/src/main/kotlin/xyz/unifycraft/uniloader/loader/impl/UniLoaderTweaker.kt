package xyz.unifycraft.uniloader.loader.impl

import net.minecraft.launchwrapper.ITweaker
import net.minecraft.launchwrapper.Launch
import net.minecraft.launchwrapper.LaunchClassLoader
import org.spongepowered.asm.launch.MixinBootstrap
import org.spongepowered.asm.mixin.MixinEnvironment
import xyz.unifycraft.uniloader.loader.UniLoader
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

abstract class UniLoaderTweaker : ITweaker {
    private val launchArgs = mutableMapOf<String, String>()
    private val isPrimaryTweaker = (Launch.blackboard["Tweaks"] as? List<ITweaker>)?.isEmpty() == true

    private val classPath = mutableListOf<Path>()

    abstract override fun getLaunchTarget(): String
    abstract fun getEnvironment(): Environment

    override fun acceptOptions(args: MutableList<String>, gameDir: File, assetsDir: File, profile: String) {
        for (arg in args) {
            val index = args.indexOf(arg)
            if (arg.startsWith("--")) {
                var shouldContinue = false
                var value = args.get(index + 1)
                if (value.startsWith("--")) { // This is an empty arg
                    value = ""
                } else shouldContinue = true
                launchArgs[arg.substring(2)] = value
                if (shouldContinue) continue
            }
        }
    }

    override fun injectIntoClassLoader(classLoader: LaunchClassLoader) {
        for (source in classLoader.sources) {
            val path = Paths.get(source.toURI())
            if (!Files.exists(path)) continue
            classPath.add(path)
        }

        val bridge = MinecraftBridgeImpl(launchArgs)
        UniLoader.getInstance().setBridge(bridge)

        MixinBootstrap.init()
        MixinEnvironment.getDefaultEnvironment().side =
            if (getEnvironment() == Environment.CLIENT) MixinEnvironment.Side.CLIENT else MixinEnvironment.Side.SERVER
    }

    override fun getLaunchArguments() =
        if (isPrimaryTweaker) arrayOf<String>() else arrayOf<String>()
}
