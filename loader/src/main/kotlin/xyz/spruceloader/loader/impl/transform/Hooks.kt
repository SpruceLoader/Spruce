package xyz.spruceloader.loader.impl.transform

/*import org.slf4j.LoggerFactory
import xyz.spruceloader.api.ClientModEntrypoint
import xyz.spruceloader.api.ServerModEntrypoint
import xyz.spruceloader.loader.api.SpruceLoader

@Suppress("unused")
object Hooks {
    private val logger = LoggerFactory.getLogger("${SpruceLoader.NAME} Hooks")
    val internalName = Hooks::class.java.name.replace('.', '/')

    @JvmStatic
    fun fetchBranding(original: String?): String {
        if (original.isNullOrBlank()) {
            logger.warn("Original brand name was either empty or null!")
            return SpruceLoader.NAME
        }

        return if (BrandingTransformer.VANILLA == original) SpruceLoader.NAME else "$original,${SpruceLoader.NAME}"
    }

    @JvmStatic
    fun handleClient() {
        SpruceLoader.getInstance().getEntrypoints<ClientModEntrypoint>("client").forEach { entrypoint ->
            entrypoint.initialize()
        }
    }
    @JvmStatic
    
    fun handleServer() {
        SpruceLoader.getInstance().getEntrypoints<ServerModEntrypoint>("server").forEach { entrypoint ->
            entrypoint.initialize()
        }
    }
    @JvmStatic

    fun openWebsite() {
        println("*OPENS WEBITE*")
    }
}*/
