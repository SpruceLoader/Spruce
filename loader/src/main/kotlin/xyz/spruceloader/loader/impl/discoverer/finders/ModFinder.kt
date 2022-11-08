package xyz.spruceloader.loader.impl.discoverer.finders

import java.io.File

interface ModFinder {
    fun find(): List<File>
}
