package xyz.unifycraft.uniloader.loader.impl.transform

import xyz.unifycraft.bytetransform.annotaions.Redirect
import xyz.unifycraft.bytetransform.annotaions.Transformer

@Transformer(target = ["net.minecraft.client.ClientBrandRetriever", "net.minecraft.server.MinecraftServer"])
class BrandingTransformation {

    @Redirect(target = ["getClientModName", "getServerModName"])
    fun modifyBranding(original: String?) = Hooks.fetchBranding(original)

}
