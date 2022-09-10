package xyz.unifycraft.uniloader.loader.impl.discoverer.finders

import java.io.File

interface ModFinder {
    fun find(): List<File>
}
