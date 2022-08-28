package xyz.unifycraft.uniloader.loader

import xyz.unifycraft.uniloader.loader.impl.UniLoaderImpl
import xyz.unifycraft.uniloader.loader.impl.loading.ModClassLoader
import xyz.unifycraft.uniloader.utils.AbstractionHelper
import java.io.File

interface UniLoader {
    fun <T> getEntrypoints(namespace: String, type: Class<T>): List<T>
    fun isModLoaded(id: String): Boolean
    fun isDevelopmentEnvironment(): Boolean

    fun getDataDir(): File
    fun getConfigDir(): File
    fun getModsDir(): File

    fun getClassLoader(): ModClassLoader
    fun setClassLoader(classLoader: ModClassLoader)

    companion object {
        private lateinit var instance: UniLoader

        @JvmStatic
        fun getInstance(): UniLoader {
            if (!::instance.isInitialized) instance =
                AbstractionHelper.create(UniLoaderImpl::class.java, "uniloader.loader", true)
            return instance
        }
    }
}
