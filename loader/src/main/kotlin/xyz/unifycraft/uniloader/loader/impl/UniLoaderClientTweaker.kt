package xyz.unifycraft.uniloader.loader.impl

class UniLoaderClientTweaker : UniLoaderTweaker() {
    override fun getLaunchTarget() = "net.minecraft.client.main.Main"
    override fun getEnvironment() = Environment.CLIENT
}
