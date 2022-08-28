package xyz.unifycraft.uniloader.loader

import org.apache.logging.log4j.LogManager
import xyz.unifycraft.uniloader.loader.api.Environment
import xyz.unifycraft.uniloader.loader.api.UniLoader
import xyz.unifycraft.uniloader.loader.exceptions.EntrypointException
import xyz.unifycraft.uniloader.loader.impl.LaunchMainMethod
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

class Launch {
    companion object {
        private val logger = LogManager.getLogger()

        @JvmStatic
        fun start(args: Array<String>, environment: Environment) {
            val launch = Launch()
            launch.initialize(args, environment)
        }
    }

    private val classPath = mutableListOf<Path>()

    fun initialize(args: Array<String>, environment: Environment) {
        val argumentMap = ArgumentMap.parse(args)

        setupClassPath()
        if (environment == Environment.CLIENT) setupClientArgs(argumentMap)
        else setupServerArgs(argumentMap)

        /*

        try {
            for (entrypoint in UniLoader.getInstance().getEntrypoints("preLaunch", PreLaunchEntrypoint::class.java)) {
                entrypoint.initialize(argumentMap)
            }
        } catch (e: Exception) {
            throw EntrypointException("A mod caused a fatal crash during start-up!", e)
        }

         */

        launch(argumentMap)
    }

    private fun setupClassPath() {
        classPath.clear()
        val unsupportedEntries = mutableListOf<String>()
        val missingEntries = mutableListOf<String>()

        fun fromEntry(entry: String) {
            if (entry == "*" || entry.endsWith("${File.pathSeparator}*")) {
                unsupportedEntries.add(entry)
                return
            }

            val path = Paths.get(entry)
            if (!Files.exists(path)) {
                missingEntries.add(entry)
                return
            }

            classPath.add(path)
        }

        for (entry in System.getProperty(Properties.System.CLASS_PATH).split(File.pathSeparator)) fromEntry(entry)
        UniLoader.getInstance().getModsDir().list()?.toList()?.forEach(::fromEntry)

        if (unsupportedEntries.isNotEmpty()) logger.warn("UniLoader Launch does not support wildcard class path entries. The game may not load properly.\n${unsupportedEntries.joinToString("\n")}")
        if (missingEntries.isNotEmpty()) logger.warn("Class-path entries reference missing files! The game may not load properly.\n${missingEntries.joinToString("\n")}")
    }

    private fun setupClientArgs(argumentMap: ArgumentMap) {
        if (!argumentMap.hasName("accessToken")) argumentMap.put("accessToken", "UniLoader")
        if (!argumentMap.hasName("version")) argumentMap.put("version", "Unknown")
        if (!argumentMap.hasName("gameDir")) argumentMap.put("gameDir", UniLoader.getInstance().getGameDir().absolutePath)
    }

    private fun setupServerArgs(argumentMap: ArgumentMap) {
        argumentMap.remove("version")
        argumentMap.remove("gameDir")
        argumentMap.remove("assetsDir")
    }

    private fun launch(argumentMap: ArgumentMap) {
        val clz = Class.forName("net.minecraft.client.main.Main")
        val handle = LaunchMainMethod.getMainMethod(clz)
        println("Launching Minecraft!")
        handle.invokeExact(argumentMap.toArray())
    }
}
