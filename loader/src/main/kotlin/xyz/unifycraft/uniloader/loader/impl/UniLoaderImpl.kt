package xyz.unifycraft.uniloader.loader.impl

import xyz.unifycraft.uniloader.loader.api.Environment
import xyz.unifycraft.uniloader.loader.api.UniLoader
import java.io.File

class UniLoaderImpl : UniLoader {
    private lateinit var currentEnvironment: Environment

    override fun getEnvironment() = currentEnvironment
    override fun setEnvironment(environment: Environment) {
        this.currentEnvironment = environment
    }
    override fun isDev() = true

    override fun getGameDir() = File(".")
    override fun getLoaderDir() = File(getGameDir(), "UniLoader")
    override fun getConfigDir() = File(getLoaderDir(), "config")
    override fun getDataDir() = File(getLoaderDir(), "data")
    override fun getModsDir() = File("mods")
}
