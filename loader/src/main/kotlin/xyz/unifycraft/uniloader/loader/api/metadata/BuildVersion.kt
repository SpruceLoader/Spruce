package xyz.unifycraft.uniloader.loader.api.metadata

interface BuildVersion : Version {
    val build: Int
    val metadata: String?
}
