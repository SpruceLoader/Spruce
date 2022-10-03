package xyz.unifycraft.uniloader.loader.impl.transform

import org.slf4j.LoggerFactory
import xyz.unifycraft.uniloader.api.ClientModEntrypoint
import xyz.unifycraft.uniloader.api.ServerModEntrypoint
import xyz.unifycraft.uniloader.loader.api.UniLoader

@Suppress("unused")
object Hooks {
    private val logger = LoggerFactory.getLogger("${UniLoader.NAME} Hooks")
    val internalName = Hooks::class.java.name.replace('.', '/')

    @JvmStatic
    fun fetchBranding(original: String?): String {
        if (original.isNullOrBlank()) {
            logger.warn("Original brand name was either empty or null!")
            return UniLoader.NAME
        }

        return if (BrandingTransformer.VANILLA == original) UniLoader.NAME else "$original,${UniLoader.NAME}"
    }

    @JvmStatic
    fun handleClient() {
        UniLoader.getInstance().getEntrypoints<ClientModEntrypoint>("client").forEach { entrypoint ->
            entrypoint.initialize()
        }
    }
    @JvmStatic
    
    fun handleServer() {
        UniLoader.getInstance().getEntrypoints<ServerModEntrypoint>("server").forEach { entrypoint ->
            entrypoint.initialize()
        }
    }
    @JvmStatic

    fun openWebsite() {
        println("*OPENS WEBITE*")
    }
}
