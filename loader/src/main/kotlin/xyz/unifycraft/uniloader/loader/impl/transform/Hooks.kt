package xyz.unifycraft.uniloader.loader.impl.transform

import org.apache.logging.log4j.LogManager
import xyz.unifycraft.uniloader.api.ClientModEntrypoint
import xyz.unifycraft.uniloader.loader.api.UniLoader

@Suppress("unused")
object Hooks {
    private val logger = LogManager.getLogger("UniLoader Hooks")
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
}
