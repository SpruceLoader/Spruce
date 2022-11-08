package xyz.spruceloader.loader.impl.config

import com.google.gson.stream.JsonWriter
import java.io.File
import java.io.StringWriter

data class LoaderConfig(
    val loadingScreen: Boolean,
    val modDirs: List<File>
) {
    companion object {
        @JvmStatic
        val DEFAULT = LoaderConfig(
            true,
            listOf()
        )

        @JvmStatic
        fun serialize(config: LoaderConfig): String {
            val stringWriter = StringWriter()
            JsonWriter(stringWriter).use { writer ->
                writer.beginObject()

                writer.name("loading_screen").value(config.loadingScreen)
                writer.name("mod_dirs")
                    .beginArray()
                config.modDirs.forEach { file ->
                    writer.value(file.absolutePath)
                }
                writer.endArray()

                writer.endObject()
            }

            return stringWriter.toString()
        }
    }
}
