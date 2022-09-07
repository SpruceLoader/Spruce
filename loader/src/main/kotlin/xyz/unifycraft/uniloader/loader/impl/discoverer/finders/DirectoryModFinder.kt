package xyz.unifycraft.uniloader.loader.impl.discoverer.finders

import java.io.File
import java.nio.file.*
import java.nio.file.attribute.BasicFileAttributes
import java.util.EnumSet

class DirectoryModFinder(
    val directory: File
) : ModFinder {
    override fun find(): List<String> {
        if (!directory.exists() && !directory.mkdirs())
            throw IllegalStateException("Failed to create directory. (${directory.absolutePath})")
        if (!directory.isDirectory)
            throw IllegalArgumentException("${directory.absolutePath} is NOT a directory!")

        val path = directory.toPath()
        val returnValue = mutableListOf<String>()
        Files.walkFileTree(path, EnumSet.of(FileVisitOption.FOLLOW_LINKS), 1, FileVisitor(returnValue))

        return returnValue
    }
}

private class FileVisitor(
    val value: MutableList<String>
) : SimpleFileVisitor<Path>() {
    override fun visitFile(file: Path, attrs: BasicFileAttributes): FileVisitResult {
        if (file.isModFile())
            value.add(file.toFile().readText())

        return FileVisitResult.CONTINUE
    }

    private fun Path.isModFile(): Boolean {
        if (!Files.isRegularFile(this))
            return false

        try {
            if (Files.isHidden(this))
                return false
        } catch (t: Throwable) {
            return false
        }

        val fileName = fileName.toString()
        return fileName.endsWith(".jar") && !fileName.startsWith(".")
    }
}
